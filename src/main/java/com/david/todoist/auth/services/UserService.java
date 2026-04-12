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

    
    
    public void deleteByUsername(String username) {
      userRepo.deleteByUsername(username);
    }
    
    public AppUser findByEmail(String email) {
      return userRepo.findUserByEmail(email);
    }

    public boolean verifyPassword(String encodedPassword, String rawPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
      return userRepo.findUserByUsername(username);
    }
}
