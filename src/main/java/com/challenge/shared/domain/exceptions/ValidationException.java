package com.challenge.shared.domain.exceptions;

/**
 * Base exception for validation errors
 * Should return HTTP 400 Bad Request
 */
public class ValidationException extends RuntimeException {
  
  public ValidationException(String message) {
    super(message);
  }
}
