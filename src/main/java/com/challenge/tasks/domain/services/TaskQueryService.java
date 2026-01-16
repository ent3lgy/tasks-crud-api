package com.challenge.tasks.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import com.challenge.tasks.domain.model.aggregates.Task;
import com.challenge.tasks.domain.model.queries.GetAllTasksQuery;
import com.challenge.tasks.domain.model.queries.GetTaskByIdQuery;
import com.challenge.tasks.domain.model.queries.GetTaskStatsQuery;

/**
 * Service to query tasks
 * @summary
 * This service is responsible for querying tasks. It provides methods to get all tasks, get a task by its ID and get task statistics.
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TaskQueryService {

  /**
   * Get all tasks
   * @param query The query to get all tasks
   * @return The list of tasks
   */
  List<Task> handle(GetAllTasksQuery query);

  /**
   * Get a task by its ID
   * @param query The query to get a task by its ID
   * @return The task
   */
  Optional<Task> handle(GetTaskByIdQuery query);

  /**
   * Get task statistics
   * @param query The query to get task statistics
   * @return Map with statistics
   */
  Map<String, Object> handle(GetTaskStatsQuery query);
}
