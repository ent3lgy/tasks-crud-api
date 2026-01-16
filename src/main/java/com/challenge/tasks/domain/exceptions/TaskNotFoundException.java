package com.challenge.tasks.domain.exceptions;

import com.challenge.shared.domain.exceptions.ResourceNotFoundException;

/**
 * Task Not Found Exception
 * @summary
 * This exception is thrown when a task is not found.
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public class TaskNotFoundException extends ResourceNotFoundException {
  
  public TaskNotFoundException(Long taskId) {
    super("Task", taskId);
  }
}
