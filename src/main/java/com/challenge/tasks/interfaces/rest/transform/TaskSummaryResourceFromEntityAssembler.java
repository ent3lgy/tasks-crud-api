package com.challenge.tasks.interfaces.rest.transform;

import com.challenge.tasks.domain.model.aggregates.Task;
import com.challenge.tasks.interfaces.rest.resources.TaskSummaryResource;

/**
 * Assembler to convert Task entity to TaskSummaryResource
 */
public class TaskSummaryResourceFromEntityAssembler {

  /**
   * Convert Task entity to TaskSummaryResource
   * @param task The task entity
   * @return The summary resource
   */
  public static TaskSummaryResource toSummaryFromEntity(Task task) {
    return new TaskSummaryResource(
        task.getId(),
        task.getTitle(),
        task.getDueDate(),
        task.getPriority().name(),
        task.getStatus().name()
    );
  }
}
