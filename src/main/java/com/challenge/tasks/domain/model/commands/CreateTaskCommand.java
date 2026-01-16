package com.challenge.tasks.domain.model.commands;

import com.challenge.tasks.domain.model.valueobjects.TaskPriority;

import java.time.LocalDate;

/**
 * Command to create a new task
 * @param title The title of the task
 * @param description The description of the task
 * @param priority The priority of the task
 * @param dueDate The due date of the task
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public record CreateTaskCommand(
  String title, 
  String description, 
  TaskPriority priority, 
  LocalDate dueDate 
) {
}
