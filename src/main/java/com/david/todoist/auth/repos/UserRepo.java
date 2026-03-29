package com.david.todoist.auth.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.david.todoist.auth.AppUser;

@Repository
public interface UserRepo extends JpaRepository<AppUser, Long> {
    public AppUser findUserByUsername(String username);

    public AppUser deleteByUsername(String username);
}
