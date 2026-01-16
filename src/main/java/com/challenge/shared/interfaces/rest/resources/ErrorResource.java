package com.challenge.shared.interfaces.rest.resources;

import java.time.LocalDateTime;

/**
 * Resource representing an error response
 * @param timestamp The timestamp of the error
 * @param status The HTTP status code
 * @param message The error message
 * @param path The request path that caused the error
 */
public record ErrorResource(
  LocalDateTime timestamp,
  int status,
  String message,
  String path
) {
}
