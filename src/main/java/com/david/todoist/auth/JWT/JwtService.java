package com.david.todoist.auth.JWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private Map<String, Object> claims = new HashMap<>();
    private SecretKey secretKey;
    private SecretKey alt;

    public JwtService() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        this.secretKey = keyGenerator.generateKey();
        this.alt = keyGenerator.generateKey();
    }

    public String generateToken(String username) {
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

    public void rotateKey() {
        var temp = alt;
        alt = secretKey;
        secretKey = temp;
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

    private void thing(String token) {
        Claims claims = extractAllClaims(token);
        
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
