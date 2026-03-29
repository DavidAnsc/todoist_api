package com.david.todoist.auth.JWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.david.todoist.auth.AppUser;
import com.david.todoist.auth.services.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private Map<String, Object> claims = new HashMap<>();
    private SecretKey secretKey;

    @Autowired
    private UserService userService;
    @Autowired
    private JTokenRepo jTokenRepo;

    public boolean isTokenBlacklisted(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        return jTokenRepo.findAll().stream()
                .anyMatch(object -> token.equals(object.getToken()));
    }

    public void blacklistToken(String username, String token) {
        AppUser user = userService.findByUsername(username);
        JToken jToken = new JToken();

        jToken.setUser(user);
        jToken.setToken(token);
        jToken.setExpiry(this.extractAllClaims(token).getExpiration());
        
        jTokenRepo.save(jToken);
    }

    public synchronized void refreshList() {
        jTokenRepo.findAll().forEach(object -> {
            if (object.getExpiry().before(new Date())) {
                jTokenRepo.delete(object);
            }
        });
    }


    public JwtService() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        this.secretKey = keyGenerator.generateKey();
    }

    public String generateToken(String username) {
        AppUser user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Couldn't find the user based on the username {auth/JWT/JwtService.java}");
        }

        return Jwts.builder()
                .claims()
                    .add(claims)
                    .subject(username)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 4))
                .and()
                    .signWith(secretKey)
                .compact();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username != null
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
