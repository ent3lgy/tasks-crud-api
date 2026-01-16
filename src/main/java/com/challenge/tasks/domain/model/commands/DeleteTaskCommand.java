package com.challenge.tasks.domain.model.commands;

/**
 * Command to delete a task by its ID
 * @param taskId The ID of the task to delete
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
public record DeleteTaskCommand(Long taskId) {
}
