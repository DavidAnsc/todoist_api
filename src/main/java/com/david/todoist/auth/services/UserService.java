package com.david.todoist.auth.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.david.todoist.auth.AppUser;
import com.david.todoist.auth.repos.UserRepo;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public AppUser save(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }


    public AppUser deleteByUsername(String username) {
        return userRepo.deleteByUsername(username);
    }

    public AppUser findByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }

    public boolean verifyPassword(String encodedPassword, String rawPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepo.findUserByUsername(username) != null) {
            return userRepo.findUserByUsername(username);
        }
        throw new UsernameNotFoundException("User not found {auth/services/UserService.java}");
    }
}
