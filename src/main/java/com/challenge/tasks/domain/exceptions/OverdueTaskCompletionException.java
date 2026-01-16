package com.challenge.tasks.domain.exceptions;

import com.challenge.shared.domain.exceptions.BusinessRuleException;

/**
 * Overdue Task Completion Exception
 * @summary
 * This exception is thrown when trying to mark an overdue task as DONE.
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public class OverdueTaskCompletionException extends BusinessRuleException {
  
  public OverdueTaskCompletionException() {
    super("Cannot complete an overdue task.");
  }
  
  public OverdueTaskCompletionException(Long taskId) {
    super("Cannot complete an overdue task. Task ID: " + taskId);
  }
}
