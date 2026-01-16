package com.challenge.tasks.domain.model.commands;

import com.challenge.tasks.domain.model.valueobjects.TaskStatus;

/**
 * Command to update the status of a task by its ID
 * @param taskId The ID of the task to update
 * @param newStatus The new status of the task
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public record UpdateTaskStatusCommand(
  Long taskId,
  TaskStatus newStatus
) {
}
