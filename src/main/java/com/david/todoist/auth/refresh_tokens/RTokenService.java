package com.david.todoist.auth.refresh_tokens;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.david.todoist.auth.AppUser;
import com.david.todoist.auth.services.UserService;

@Service
public class RTokenService {
    @Autowired
    private RTokenRepo rTokenRepo;
    @Autowired
    private UserService userService;

    public RefreshToken findByToken(String token) {
        return rTokenRepo.findByToken(token);
    }

    public String createToken(String username) {
        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpiry(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48));
        
        AppUser user = userService.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("Username not found {/auth/refresh_tokens/RTokenService.java}");
        }
        token.setUser(user);
        rTokenRepo.save(token);
        return token.getToken();
    }

    public String refreshToken(String username) {
        this.deleteToken(username);
        return this.createToken(username);
    }

    public void deleteToken(String username) {
        rTokenRepo.findAll().stream().forEach(object -> {
            if (object.getUser().getUsername() == username) {
                RefreshToken token = object;
                rTokenRepo.delete(token);
            }
        });
    }
}
