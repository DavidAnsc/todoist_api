package com.david.todoist.auth.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.david.todoist.auth.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    jwtService.refreshList();

    if (header != null && header.startsWith("Bearer ")) {
      String jwtToken = header.substring(7);
      UserDetails user = userService.loadUserByUsername(jwtService.extractUsername(jwtToken));

      if (jwtService.validateToken(jwtToken, user) && SecurityContextHolder.getContext().getAuthentication() == null
          && !jwtService.isTokenBlacklisted(jwtToken)) {
        UsernamePasswordAuthenticationToken authObject = new UsernamePasswordAuthenticationToken(user, null,
            user.getAuthorities());
        authObject.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authObject);
      }
    } 

    filterChain.doFilter(request, response);
  }
}
