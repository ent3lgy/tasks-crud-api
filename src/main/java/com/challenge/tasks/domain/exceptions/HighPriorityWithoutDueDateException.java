package com.challenge.tasks.domain.exceptions;

import com.challenge.shared.domain.exceptions.ValidationException;

/**
 * High Priority Without Due Date Exception
 * @summary
 * This exception is thrown when creating/updating a task with HIGH priority without dueDate.
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public class HighPriorityWithoutDueDateException extends ValidationException {
  
  public HighPriorityWithoutDueDateException() {
    super("For tasks with HIGH priority, the due date is mandatory.");
  }
}
