package com.challenge.tasks.interfaces.rest.transform;

import com.challenge.tasks.domain.model.commands.UpdateTaskCommand;
import com.challenge.tasks.interfaces.rest.resources.UpdateTaskResource;

/**
 * Assembler to convert UpdateTaskResource to UpdateTaskCommand
 */
public class UpdateTaskCommandFromResourceAssembler {

  /**
   * Convert UpdateTaskResource and taskId to UpdateTaskCommand
   * @param resource The resource to convert
   * @param taskId The task ID
   * @return The command
   */
  public static UpdateTaskCommand toCommandFromResource(UpdateTaskResource resource, Long taskId) {
    return new UpdateTaskCommand(
        taskId,
        resource.title(),
        resource.description(),
        resource.priority(),
        resource.dueDate(),
        resource.status()
    );
  }
}
