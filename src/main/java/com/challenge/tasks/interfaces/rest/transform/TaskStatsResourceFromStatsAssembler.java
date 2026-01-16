package com.challenge.tasks.interfaces.rest.transform;

import com.challenge.tasks.domain.model.valueobjects.TaskStatistics;
import com.challenge.tasks.interfaces.rest.resources.TaskStatsResource;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Assembler to convert TaskStatistics value object to TaskStatsResource
 */
public class TaskStatsResourceFromStatsAssembler {

  /**
   * Convert TaskStatistics value object to TaskStatsResource
   * @param statistics The TaskStatistics value object
   * @return The resource
   */
  public static TaskStatsResource toResourceFromStats(TaskStatistics statistics) {
    var next7Days = statistics.next7Days().stream()
        .map(TaskSummaryResourceFromEntityAssembler::toSummaryFromEntity)
        .collect(Collectors.toList());

    var byStatusMap = statistics.byStatus().entrySet().stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().name(),
            Map.Entry::getValue
        ));

    var byPriorityMap = statistics.byPriority().entrySet().stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().name(),
            Map.Entry::getValue
        ));

    return new TaskStatsResource(
        statistics.total(),
        byStatusMap,
        byPriorityMap,
        statistics.overdue(),
        next7Days
    );
  }
}
