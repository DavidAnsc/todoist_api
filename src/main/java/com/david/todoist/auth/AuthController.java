package com.david.todoist.auth;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.todoist.auth.JWT.JwtService;
import com.david.todoist.auth.expt_handling.UnprocessableBodyException;
import com.david.todoist.auth.refresh_tokens.RTokenService;
import com.david.todoist.auth.response.ResponseObject;
import com.david.todoist.auth.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

  Object registerLock = new Object();

  @Autowired
  private UserService service;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private RTokenService rTokenService;

  @PostMapping("/register")
  public UserDetails register(@RequestBody AppUser entity) {
    synchronized (registerLock) {
      if (service.loadUserByUsername(entity.getUsername()) == null) {
        return service.save(entity);
      } else {
        throw new InvalidDataAccessApiUsageException("607 User already exists.");
      }
    }
  }

  @GetMapping("/getUserInfo")
  public AppUser getInfo(HttpServletRequest httpRequest) {
    String jToken = httpRequest.getHeader("Authorization").substring(7);

    String username = jwtService.extractUsername(jToken);
    AppUser user = service.loadUserByUsername(username);

    if (user == null) {
      throw new BadCredentialsException("The username doesn't exist.");
    }

    return user;
  }

  @PostMapping("/login")
  public ResponseObject login(@RequestBody UserLoginDTO entity, HttpServletResponse httpResponse) {
    String email = null;
    String username = null;
    if (entity.getUsernameOrEmail().contains("@")) {
      email = entity.getUsernameOrEmail();
      username = null;
    } else {
      username = entity.getUsernameOrEmail();
      email = null;
    }
    System.out.println(email);
    System.out.println(username);

    if (email == null && username != null) {
      AppUser user = service.loadUserByUsername(entity.getUsernameOrEmail());

      if (service.verifyPassword(user.getPassword(), entity.getPassword())) {
        if (username.equals(user.getUsername())) {
          ResponseObject response = new ResponseObject();
          response.setJwtToken(jwtService.generateToken(entity.getUsernameOrEmail()));

          Cookie refreshCookie = new Cookie("refreshToken", rTokenService.createToken(entity.getUsernameOrEmail()));
          refreshCookie.setHttpOnly(true);
          // refreshCookie.setSecure(true); // only over HTTPS TODO: turn this on in
          // production
          refreshCookie.setPath("/");
          refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

          httpResponse.addCookie(refreshCookie);

          return response;
        } else {
          throw new BadCredentialsException("The username doesn't match.");
        }
      } else {
        throw new BadCredentialsException("The password is incorrect.");
      }
    } else {
      AppUser user = service.findByEmail(email);
      // if (user == null) {
      //   throw new BadCredentialsException("The email doesn't exist.");
      // }
      System.out.println(user);

      if (service.verifyPassword(user.getPassword(), entity.getPassword())) {
        if (email.equals(user.getEmail())) {
          ResponseObject response = new ResponseObject();
          response.setJwtToken(jwtService.generateToken(user.getUsername()));
          
          Cookie refreshCookie = new Cookie("refreshToken", rTokenService.createToken(user.getUsername()));
          refreshCookie.setHttpOnly(true);
          // refreshCookie.setSecure(true); // only over HTTPS TODO: turn this on in
          // production
          refreshCookie.setPath("/");
          refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
          
          httpResponse.addCookie(refreshCookie);
          
          return response;
        } else {
          throw new BadCredentialsException("The email doesn't match.");
        }
      } else {
        throw new BadCredentialsException("The password is incorrect.");
      }
    }
  }

  @PostMapping("/refresh")
  public ResponseObject refreshJWTToken(@RequestBody Map<String, String> entity, HttpServletRequest httpRequest)
      throws Exception {
    Optional<String> jwtToken = entity.containsKey("jwt") ? Optional.of(entity.get("jwt")) : Optional.empty();
    Cookie[] refreshCookies = httpRequest.getCookies();

    rTokenService.refreshDbStatus();

    if (refreshCookies.length == 0 || refreshCookies == null) {
      throw new MissingRequestCookieException("There's no cookie info contained.", null);
    }

    String refreshToken = null;

    for (Cookie cookie : refreshCookies) {
      if ("refreshToken".equals(cookie.getName())) {
        refreshToken = cookie.getValue();
      }
    }

    if (refreshToken == null || refreshToken.isBlank()) {
      throw new InvalidCookieException("Can't find cookie refreshToken");
    }

    if (rTokenService.findByToken(refreshToken) == null || refreshToken == null) {
      throw new IllegalArgumentException(
          "Can't extract 'refreshToken' from Cookie. {/auth/controllers/AuthController.java}");
    }

    String username;
    if (jwtToken.isEmpty()) {
      username = rTokenService.findByToken(refreshToken).getUser().getUsername();
    } else {
      username = jwtService.extractUsername(jwtToken.get());
      jwtService.blacklistToken(jwtService.extractUsername(jwtToken.get()), jwtToken.get());
    }

    ResponseObject response = new ResponseObject();
    response.setJwtToken(jwtService.generateToken(username));
    response.setRefreshToken(null);
    return response;
  }

  @GetMapping("/logout")
  public ResponseObject logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
    Cookie[] refreshCookies = httpRequest.getCookies();
    Cookie refreshToken = new Cookie("refreshToken", null);
    String jToken = httpRequest.getHeader("Authorization").substring(7);

    rTokenService.refreshDbStatus();

    if (refreshCookies.length == 0 || refreshCookies == null) {
      throw new MissingRequestCookieException("There's no cookie info contained.", null);
    }

    String token = null;

    for (Cookie cookie : refreshCookies) {
      if ("refreshToken".equals(cookie.getName())) {
        token = cookie.getValue();
      }
    }

    if (token == null || token.isBlank()) {
      throw new InvalidCookieException("Can't find cookie refreshToken");
    }

    if (rTokenService.findByToken(token) == null || token == null) {
      throw new IllegalArgumentException(
          "Can't extract 'refreshToken' from Cookie. {/auth/controllers/AuthController.java}");
    }

    if (!rTokenService.findByToken(token).getUser().getUsername().equals(jwtService.extractUsername(jToken))) {
      throw new UnprocessableBodyException(
          "The username in the refresh token doesn't match the username in the JWT token.");
    }

    refreshToken.setHttpOnly(true);
    refreshToken.setPath("/");
    refreshToken.setMaxAge(0);
    httpResponse.addCookie(refreshToken);

    AppUser user = rTokenService.findByToken(token).getUser();
    rTokenService.deleteToken(user.getUsername());
    jwtService.blacklistToken(user.getUsername(), jToken);
    return new ResponseObject();
  }

  @GetMapping("/test")
  public String test() {
    return "Hello, JWT token validation passed.";
  }

}
