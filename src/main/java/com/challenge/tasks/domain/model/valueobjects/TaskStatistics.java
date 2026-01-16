package com.challenge.tasks.domain.model.valueobjects;

import java.util.Map;
import java.util.List;
import com.challenge.tasks.domain.model.aggregates.Task;

/**
 * Task statistics
 * @summary
 * This record represents the statistics of a task. It contains the total number of tasks, the number of tasks by status, the number of tasks by priority, the number of overdue tasks and the tasks due in the next 7 days.
 * <ul>
 *   <li>total: The total number of tasks.</li>
 *   <li>byStatus: The number of tasks by status.</li>
 *   <li>byPriority: The number of tasks by priority.</li>
 *   <li>overdue: The number of overdue tasks.</li>
 *   <li>next7Days: The tasks due in the next 7 days.</li>
 * </ul>
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public record TaskStatistics(
  Long total,
  Map<TaskStatus, Long> byStatus,
  Map<TaskPriority, Long> byPriority,
  Long overdue,
  List<Task> next7Days
) {
}
