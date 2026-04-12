package com.david.todoist.auth.response;

public class ResponseObject {
  private String jwtToken;
  private String refreshToken;

  public ResponseObject() {}

  public String getJwtToken() {
    return jwtToken;
  }
  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }
  public String getRefreshToken() {
    return refreshToken;
  }
  public void setRefreshToken(String updateToken) {
    this.refreshToken = updateToken;
  }
}
