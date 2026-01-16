package com.challenge.tasks.interfaces.rest.resources;

import java.time.LocalDate;

/**
 * Simplified task resource for statistics
 * @param id Task ID
 * @param title Task title
 * @param dueDate Due date
 * @param priority Task priority
 * @param status Task status
 */
public record TaskSummaryResource(
  Long id,
  String title,
  LocalDate dueDate,
  String priority,
  String status
) {
}
