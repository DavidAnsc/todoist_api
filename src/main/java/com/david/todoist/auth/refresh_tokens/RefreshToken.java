package com.david.todoist.auth.refresh_tokens;

import java.util.Date;

import com.david.todoist.auth.AppUser;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long Id;

    private String token;

    private Date expiry;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;


    public RefreshToken() {
    }
    
    
    public AppUser getUser() {
        return user;
    }
    public void setUser(AppUser user) {
        this.user = user;
    }
    public long getId() {
        return Id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Date getExpiry() {
        return expiry;
    }
    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }
}
