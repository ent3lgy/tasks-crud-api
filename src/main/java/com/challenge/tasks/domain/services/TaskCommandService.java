package com.challenge.tasks.domain.services;

import com.challenge.tasks.domain.model.aggregates.Task;
import com.challenge.tasks.domain.model.commands.CreateTaskCommand;
import com.challenge.tasks.domain.model.commands.UpdateTaskCommand;
import com.challenge.tasks.domain.model.commands.UpdateTaskStatusCommand;
import com.challenge.tasks.domain.model.commands.DeleteTaskCommand;

/**
 * Service to command tasks
 * @summary
 * This service is responsible for command tasks. It provides methods to create, update, update the status and delete a task.
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TaskCommandService {

  /**
   * Create a new task
   * @param command The command to create a new task
   * @return The created task
   */
  Task handle(CreateTaskCommand command);

  /**
   * Update a task
   * @param command The command to update a task
   */
  void handle(UpdateTaskCommand command);

  /**
   * Update the status of a task
   * @param command The command to update the status of a task
   */
  void handle(UpdateTaskStatusCommand command);

  /**
   * Delete a task
   * @param command The command to delete a task
   */
  void handle(DeleteTaskCommand command);
}
