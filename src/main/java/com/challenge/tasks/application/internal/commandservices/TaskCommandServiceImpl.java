package com.challenge.tasks.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.challenge.tasks.domain.model.aggregates.Task;
import com.challenge.tasks.domain.services.TaskCommandService;
import com.challenge.tasks.domain.model.valueobjects.TaskStatus;
import com.challenge.tasks.domain.model.valueobjects.TaskPriority;
import com.challenge.tasks.domain.model.commands.CreateTaskCommand;
import com.challenge.tasks.domain.model.commands.UpdateTaskCommand;
import com.challenge.tasks.domain.model.commands.DeleteTaskCommand;
import com.challenge.tasks.domain.exceptions.TaskNotFoundException;
import com.challenge.tasks.domain.model.commands.UpdateTaskStatusCommand;
import com.challenge.tasks.domain.exceptions.OverdueTaskCompletionException;
import com.challenge.tasks.domain.exceptions.HighPriorityWithoutDueDateException;
import com.challenge.tasks.infrastructure.persistence.jpa.repositories.TaskRepository;

import java.time.LocalDate;

/**
 * Implementation of the Task Command Service
 */
@Service
@Transactional
public class TaskCommandServiceImpl implements TaskCommandService {

  private final TaskRepository taskRepository;

  /**
   * Constructor of the service
   * @param taskRepository The repository to use
   */
  public TaskCommandServiceImpl(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  // {@inheritDoc}
  @Override
  public Task handle(CreateTaskCommand command) {
    validateHighPriorityDueDate(command.priority(), command.dueDate());
    
    var task = new Task(
        command.title(),
        command.description(),
        command.priority(),
        command.dueDate()
    );
    
    if (command.status() != null && !TaskStatus.TODO.equals(command.status())) {
      task.updateStatus(command.status());
    }
    
    return taskRepository.save(task);
  }

  // {@inheritDoc}
  @Override
  public void handle(UpdateTaskCommand command) {
    var task = taskRepository.findById(command.taskId())
        .orElseThrow(() -> new TaskNotFoundException(command.taskId()));
    
    validateHighPriorityDueDate(command.priority(), command.dueDate());
    
    if (TaskStatus.DONE.equals(command.status()) && task.isOverdue()) {
      throw new OverdueTaskCompletionException(command.taskId());
    }
    
    task.update(
        command.title(),
        command.description(),
        command.priority(),
        command.dueDate(),
        command.status()
    );
    
    taskRepository.save(task);
  }

  // {@inheritDoc}
  @Override
  public void handle(UpdateTaskStatusCommand command) {
    var task = taskRepository.findById(command.taskId())
        .orElseThrow(() -> new TaskNotFoundException(command.taskId()));
    
    if (TaskStatus.DONE.equals(command.newStatus()) && task.isOverdue()) {
      throw new OverdueTaskCompletionException(command.taskId());
    }
    
    task.updateStatus(command.newStatus());
    
    taskRepository.save(task);
  }

  // {@inheritDoc}
  @Override
  public void handle(DeleteTaskCommand command) {
    if (!taskRepository.existsById(command.taskId())) {
      throw new TaskNotFoundException(command.taskId());
    }
    
    taskRepository.deleteById(command.taskId());
  }

  /**
   * Validates that HIGH priority tasks have a dueDate
   * Business Rule B: For priority HIGH, dueDate is mandatory
   * @param priority The task priority
   * @param dueDate The task due date
   */
  private void validateHighPriorityDueDate(TaskPriority priority, LocalDate dueDate) {
    if (TaskPriority.HIGH.equals(priority) && dueDate == null) {
      throw new HighPriorityWithoutDueDateException();
    }
  }
}
