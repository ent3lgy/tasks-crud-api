package com.challenge.tasks.interfaces.rest.resources;

import com.challenge.tasks.domain.model.valueobjects.TaskPriority;
import com.challenge.tasks.domain.model.valueobjects.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Resource representing a task
 * @param id The ID of the task
 * @param title The title of the task
 * @param description The description of the task
 * @param status The status of the task
 * @param priority The priority of the task
 * @param dueDate The due date of the task
 * @param createdAt The creation date of the task
 * @param updatedAt The last update date of the task
 */
public record TaskResource(
  Long id,
  String title,
  String description,
  TaskStatus status,
  TaskPriority priority,
  LocalDate dueDate,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {
}
