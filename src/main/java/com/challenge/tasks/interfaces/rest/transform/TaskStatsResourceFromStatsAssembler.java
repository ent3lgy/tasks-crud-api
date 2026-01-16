package com.challenge.tasks.interfaces.rest.transform;

import com.challenge.tasks.domain.model.aggregates.Task;
import com.challenge.tasks.interfaces.rest.resources.TaskStatsResource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Assembler to convert statistics Map to TaskStatsResource
 */
public class TaskStatsResourceFromStatsAssembler {

  /**
   * Convert statistics Map to TaskStatsResource
   * @param stats The statistics map
   * @return The resource
   */
  @SuppressWarnings("unchecked")
  public static TaskStatsResource toResourceFromStats(Map<String, Object> stats) {
    var next7DaysTasks = (List<Task>) stats.get("next7Days");
    
    var next7Days = next7DaysTasks.stream()
        .map(TaskSummaryResourceFromEntityAssembler::toSummaryFromEntity)
        .collect(Collectors.toList());

    return new TaskStatsResource(
        ((Number) stats.get("total")).longValue(),
        (Map<String, Long>) stats.get("byStatus"),
        (Map<String, Long>) stats.get("byPriority"),
        ((Number) stats.get("overdue")).longValue(),
        next7Days
    );
  }
}
