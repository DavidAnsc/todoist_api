package com.david.todoist.auth;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

public class UserLoginDTO {
  private String usernameOrEmail;
  private String password;
  
  
  public UserLoginDTO() {
  }
  
  
  public String getPassword() {
      return password;
  }
  
  public String getUsernameOrEmail() {
      return usernameOrEmail;
  }
  
  public void setUsernameOrEmail(String username) {
      this.usernameOrEmail = username;
  }
  public void setPassword(String password) {
      this.password = password;
  }
}
