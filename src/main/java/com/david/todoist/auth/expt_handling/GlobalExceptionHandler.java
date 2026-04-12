package com.david.todoist.auth.expt_handling;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(UnprocessableBodyException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
  public APIError handleUnprocessableException(UnprocessableBodyException err, HttpServletRequest request) {
    APIError error = new APIError();
    error.setStatus(422);
    error.setError("Unprocessable Content");
    error.setMessage(err.getMessage());
    return error;
  }

  @ExceptionHandler(InvalidDataAccessApiUsageException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public APIError handleInvalidDataAccess(InvalidDataAccessApiUsageException err, HttpServletRequest request) {
    APIError error = new APIError();
    error.setStatus(422);
    error.setError("Invalid Data Access");
    error.setMessage(err.getMessage());
    return error;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public APIError handleIllegalArgument(IllegalArgumentException err, HttpServletRequest request) {
    APIError error = new APIError();
    error.setStatus(400);
    error.setError("Bad Request");
    error.setMessage(err.getMessage());
    return error;
  }

  @ExceptionHandler(InvalidCookieException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public APIError handleInvalidCookie(InvalidCookieException err, HttpServletRequest request) {
    APIError error = new APIError();
    error.setStatus(401);
    error.setError("Invalid Cookie");
    error.setMessage(err.getMessage());
    return error;
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public APIError handleBadCredentials(BadCredentialsException err, HttpServletRequest request) {
    APIError error = new APIError();
    error.setStatus(401);
    error.setError("Bad Credentials");
    error.setMessage(err.getMessage());
    return error;
  }

  @ExceptionHandler(SignatureException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public APIError handleSignatureError(SignatureException err, HttpServletRequest request) {
    APIError error = new APIError();
    error.setStatus(401);
    error.setError("Invalid JWT Signature");
    error.setMessage(err.getMessage());
    return error;
  }

  @ExceptionHandler(exception = Unauthorized.class)
  public APIError handleUnauthorizedError(Unauthorized err, HttpServletRequest request) {
    APIError error = new APIError();
    error.setStatus(401);
    error.setError("Unauthorized");
    error.setMessage(err.getMessage());
    return error;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public APIError handleGeneralException(Exception err, HttpServletRequest request) {
    APIError error = new APIError();
    error.setStatus(500);
    error.setError("Internal Server Error");
    error.setMessage(err.getMessage());
    return error;
  }
}