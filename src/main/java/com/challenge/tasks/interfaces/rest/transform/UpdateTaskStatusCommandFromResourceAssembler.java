package com.challenge.tasks.interfaces.rest.transform;

import com.challenge.tasks.domain.model.commands.UpdateTaskStatusCommand;
import com.challenge.tasks.interfaces.rest.resources.UpdateTaskStatusResource;

/**
 * Assembler to convert UpdateTaskStatusResource to UpdateTaskStatusCommand
 */
public class UpdateTaskStatusCommandFromResourceAssembler {

  /**
   * Convert UpdateTaskStatusResource and taskId to UpdateTaskStatusCommand
   * @param resource The resource to convert
   * @param taskId The task ID
   * @return The command
   */
  public static UpdateTaskStatusCommand toCommandFromResource(UpdateTaskStatusResource resource, Long taskId) {
    return new UpdateTaskStatusCommand(
        taskId,
        resource.status()
    );
  }
}
