package com.challenge.shared.interfaces.rest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.challenge.shared.domain.exceptions.ValidationException;
import com.challenge.shared.domain.exceptions.BusinessRuleException;
import com.challenge.shared.interfaces.rest.resources.ErrorResource;
import com.challenge.shared.domain.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Global Exception Handler
 * @summary
 * This class is responsible for handling exceptions across the entire application.
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle ResourceNotFoundException - 404 Not Found
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResource> handleResourceNotFoundException(
      ResourceNotFoundException ex, 
      HttpServletRequest request) {
    
    var error = new ErrorResource(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        request.getRequestURI()
    );
    
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  /**
   * Handle BusinessRuleException - 409 Conflict
   */
  @ExceptionHandler(BusinessRuleException.class)
  public ResponseEntity<ErrorResource> handleBusinessRuleException(
      BusinessRuleException ex, 
      HttpServletRequest request) {
    
    var error = new ErrorResource(
        LocalDateTime.now(),
        HttpStatus.CONFLICT.value(),
        ex.getMessage(),
        request.getRequestURI()
    );
    
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  /**
   * Handle ValidationException - 400 Bad Request
   */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResource> handleValidationException(
      ValidationException ex, 
      HttpServletRequest request) {
    
    var error = new ErrorResource(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage(),
        request.getRequestURI()
    );
    
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Handle MethodArgumentNotValidException - 400 Bad Request
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResource> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, 
      HttpServletRequest request) {
    
    // Collect all validation error messages
    var messages = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
        .collect(Collectors.joining(", "));
    
    var error = new ErrorResource(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        messages,
        request.getRequestURI()
    );
    
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Handle IllegalArgumentException - 400 Bad Request
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResource> handleIllegalArgumentException(
      IllegalArgumentException ex, 
      HttpServletRequest request) {
    
    var error = new ErrorResource(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage(),
        request.getRequestURI()
    );
    
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Handle Exception - 500 Internal Server Error
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResource> handleGenericException(
      Exception ex, 
      HttpServletRequest request) {
    
    var error = new ErrorResource(
        LocalDateTime.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal server error",
        request.getRequestURI()
    );
    
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
