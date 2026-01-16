package com.challenge.tasks.interfaces.rest.resources;

import com.challenge.tasks.domain.model.valueobjects.TaskPriority;
import com.challenge.tasks.domain.model.valueobjects.TaskStatus;

import java.time.LocalDate;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * Resource for creating a task
 * @param title The title of the task
 * @param description The description of the task
 * @param priority The priority of the task
 * @param dueDate The due date of the task
 * @param status The status of the task (optional, defaults to TODO)
 */
public record CreateTaskResource(
  @NotBlank(message = "Title is required")
  @Size(min = 3, max = 80, message = "Title must be between 3 and 80 characters")
  String title,

  @Size(max = 250, message = "Description must not exceed 250 characters")
  String description,

  @NotNull(message = "Priority is required")
  TaskPriority priority,

  LocalDate dueDate,

  TaskStatus status
) {
}
