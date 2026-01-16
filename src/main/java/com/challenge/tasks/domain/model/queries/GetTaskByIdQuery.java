package com.challenge.tasks.domain.model.queries;

/**
 * Query to get a task by its ID
 * @param taskId The ID of the task
 */
public record GetTaskByIdQuery(Long taskId) {
}
