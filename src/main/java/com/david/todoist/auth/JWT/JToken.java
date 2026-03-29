package com.david.todoist.auth.JWT;

import java.util.Date;

import com.david.todoist.auth.AppUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class JToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jtoken_id_seq")
    @SequenceGenerator(name = "jtoken_id_seq", sequenceName = "jtoken_id_seq")
    private long id;

    @Column(unique = true)
    private String token;

    private Date expiry;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;


    public JToken() {
    }

    public AppUser getUser() {
        return user;
    }
    public void setUser(AppUser user) {
        this.user = user;
    }
    public Date getExpiry() {
        return expiry;
    }
    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }
    public long getId() {
        return id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
