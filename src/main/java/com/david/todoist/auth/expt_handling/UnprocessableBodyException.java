package com.david.todoist.auth.expt_handling;

public class UnprocessableBodyException extends RuntimeException {
  public UnprocessableBodyException(String message) {
    super(message);
  }
}
