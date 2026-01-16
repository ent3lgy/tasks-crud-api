package com.challenge.tasks.domain.model.commands;

import com.challenge.tasks.domain.model.valueobjects.TaskPriority;
import com.challenge.tasks.domain.model.valueobjects.TaskStatus;

import java.time.LocalDate;

/**
 * Command to update a task by its ID
 * @param taskId The ID of the task to update
 * @param title The title of the task
 * @param description The description of the task
 * @param priority The priority of the task
 * @param dueDate The due date of the task
 * @param status The status of the task
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public record UpdateTaskCommand(
  Long taskId,
  String title,
  String description,
  TaskPriority priority,
  LocalDate dueDate,
  TaskStatus status
) {
}
