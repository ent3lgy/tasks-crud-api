package com.challenge.tasks.domain.model.queries;

import com.challenge.tasks.domain.model.valueobjects.TaskStatus;
import com.challenge.tasks.domain.model.valueobjects.TaskPriority;

/**
 * Query to get all tasks
 * @param status The status of the tasks
 * @param priority The priority of the tasks
 * @param search The search term
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public record GetAllTasksQuery(
  TaskStatus status,
  TaskPriority priority,
  String search
) {
}
