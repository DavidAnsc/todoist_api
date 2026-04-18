package com.david.todoist.auth.refresh_tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RTokenRepo extends JpaRepository<RefreshToken, Long> {
    public RefreshToken findByToken(String token);
}
