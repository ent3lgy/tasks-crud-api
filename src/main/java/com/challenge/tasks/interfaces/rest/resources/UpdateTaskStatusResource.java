package com.challenge.tasks.interfaces.rest.resources;

import com.challenge.tasks.domain.model.valueobjects.TaskStatus;

import jakarta.validation.constraints.NotNull;

/**
 * Resource for updating task status
 * @param status The new status of the task
 */
public record UpdateTaskStatusResource(
  @NotNull(message = "Status is required")
  TaskStatus status
) {
}
