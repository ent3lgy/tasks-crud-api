package com.challenge.tasks.interfaces.rest.resources;

import java.util.List;
import java.util.Map;

/**
 * Resource representing task statistics
 * @param total Total number of tasks
 * @param byStatus Count by status (TODO/IN_PROGRESS/DONE)
 * @param byPriority Count by priority (LOW/MEDIUM/HIGH)
 * @param overdue Number of overdue tasks (dueDate < today and status != DONE)
 * @param next7Days List of tasks (max 5) due in the next 7 days, ordered by dueDate asc
 */
public record TaskStatsResource(
  Long total,
  Map<String, Long> byStatus,
  Map<String, Long> byPriority,
  Long overdue,
  List<TaskSummaryResource> next7Days
) {
}
