package com.challenge.shared.domain.exceptions;

/**
 * Base exception for business rule violations
 * Should return HTTP 409 Conflict
 */
public class BusinessRuleException extends RuntimeException {
  
  public BusinessRuleException(String message) {
    super(message);
  }
}
