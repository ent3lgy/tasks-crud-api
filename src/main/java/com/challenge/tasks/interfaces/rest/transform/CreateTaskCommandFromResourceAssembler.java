package com.challenge.tasks.interfaces.rest.transform;

import com.challenge.tasks.domain.model.commands.CreateTaskCommand;
import com.challenge.tasks.interfaces.rest.resources.CreateTaskResource;

/**
 * Assembler to convert CreateTaskResource to CreateTaskCommand
 */
public class CreateTaskCommandFromResourceAssembler {

  /**
   * Convert CreateTaskResource to CreateTaskCommand
   * @param resource The resource to convert
   * @return The command
   */
  public static CreateTaskCommand toCommandFromResource(CreateTaskResource resource) {
    return new CreateTaskCommand(
        resource.title(),
        resource.description(),
        resource.priority(),
        resource.dueDate(),
        resource.status()
    );
  }
}
