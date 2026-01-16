package com.challenge.shared.domain.exceptions;

/**
 * Base exception for resource not found errors
 * Should return HTTP 404
 */
public class ResourceNotFoundException extends RuntimeException {
  
  public ResourceNotFoundException(String message) {
    super(message);
  }
  
  public ResourceNotFoundException(String resourceName, Object resourceId) {
    super(String.format("%s not found with id: %s", resourceName, resourceId));
  }
}
