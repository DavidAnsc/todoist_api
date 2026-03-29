package com.david.todoist.auth.JWT;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JTokenRepo extends JpaRepository<JToken, Long> {
    public JToken findByToken(String token);
}
