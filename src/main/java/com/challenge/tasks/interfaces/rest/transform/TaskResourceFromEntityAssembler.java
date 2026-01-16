package com.challenge.tasks.interfaces.rest.transform;

import com.challenge.tasks.domain.model.aggregates.Task;
import com.challenge.tasks.interfaces.rest.resources.TaskResource;

/**
 * Assembler to convert Task entity to TaskResource
 */
public class TaskResourceFromEntityAssembler {

  /**
   * Convert Task entity to TaskResource
   * @param task The task entity
   * @return The resource
   */
  public static TaskResource toResourceFromEntity(Task task) {
    return new TaskResource(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getStatus(),
        task.getPriority(),
        task.getDueDate(),
        task.getCreatedAt(),
        task.getUpdatedAt()
    );
  }
}
