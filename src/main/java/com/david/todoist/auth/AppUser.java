package com.david.todoist.auth;

import java.util.Arrays;
import java.util.Collection;

import javax.crypto.SecretKey;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.david.todoist.auth.JWT.JToken;
import com.david.todoist.models.TodoList;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;


@Entity
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private long id;

    @Column(unique=true)
    private String username;
    private String displayName;
    private String password;
    
    @Column(unique=true)
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    // @Column(nullable = true)
    private Collection<TodoList> todoLists;

    @OneToMany(mappedBy = "user")
    private Collection<JToken> jTokens;
    
    @Column(nullable = true)
    private Collection<? extends GrantedAuthority> authorities;
    
    
    public AppUser(Collection<? extends GrantedAuthority> authorities) {
      this.authorities = authorities;
    }
    public AppUser() {
    }
    
    
    public Collection<TodoList> getTodoLists() {
      return todoLists;
    }
    public void setTodoLists(Collection<TodoList> todoLists) {
      this.todoLists = todoLists;
    }
    public Collection<JToken> getjTokens() {
      return jTokens;
    }
    public void setjTokens(Collection<JToken> jTokens) {
      this.jTokens = jTokens;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    public long getId() {
        return id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
