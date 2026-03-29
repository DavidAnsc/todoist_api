package com.david.todoist.auth.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.todoist.auth.AppUser;
import com.david.todoist.auth.JWT.JwtService;
import com.david.todoist.auth.refresh_tokens.RTokenService;
import com.david.todoist.auth.services.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RTokenService rTokenService;


    @PostMapping("/register")
    public UserDetails register(@RequestBody AppUser entity) {
        return service.save(entity);
    }

    @PostMapping("/login")
    public String login(@RequestBody AppUser entity) {
        service.loadUserByUsername(entity.getUsername());
        return "jwt token:\n" + jwtService.generateToken(entity.getUsername()) + "\nrefresh token:\n" + rTokenService.createToken(entity.getUsername());
    }

    @PostMapping("/refresh")
    public String refreshJWTToken(@RequestBody Map<String, String> entity) {
        String token = entity.get("refreshToken");
        String username = entity.get("username");
        String password = entity.get("password");
        System.out.println(token);
        System.out.println(username);
        System.out.println(password);
        if (rTokenService.findByToken(token) == null || token.isBlank() || username.isBlank() || token == null || username == null || password.isBlank() || password == null) {
            throw new IllegalArgumentException("Can't find 'refreshToken' or/and 'username' field in the body. {/auth/controllers/AuthController.java}");
        }
        AppUser user = service.findByUsername(username);
        if (!service.verifyPassword(user.getPassword(), password)) {
            throw new IllegalArgumentException("Password incorrect. {/auth/controllers/AuthController.java}");
        }
        jwtService.rotateKey();
        return jwtService.generateToken(username);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody Map<String, String> entity) {
        String token = entity.get("refreshToken");
        String username = entity.get("username");
        String password = entity.get("password");
        if (rTokenService.findByToken(token) == null || token.isBlank() || username.isBlank() || token == null || username == null || password.isBlank() || password == null) {
            throw new IllegalArgumentException("Can't find 'refreshToken' or/and 'username' field in the body. {/auth/controllers/AuthController.java}");
        }
        AppUser user = service.findByUsername(username);
        if (!service.verifyPassword(user.getPassword(), password)) {
            throw new IllegalArgumentException("Password incorrect. {/auth/controllers/AuthController.java}");
        }
        rTokenService.deleteToken(username);
        return "Logged out successfully.";
    }
    
    @GetMapping("/test")
    public String test() {
        return "Hello, JWT token validation passed.";
    }
    
}
