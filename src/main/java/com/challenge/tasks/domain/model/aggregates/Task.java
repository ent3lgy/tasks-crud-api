package com.challenge.tasks.domain.model.aggregates;

import com.challenge.tasks.domain.model.valueobjects.TaskStatus;
import com.challenge.tasks.domain.model.valueobjects.TaskPriority;
import com.challenge.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import lombok.Getter;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

/**
 * Task Aggregate Root
 * 
 * @summary
 * This aggregate root represents a task. A task is a unit of work that is assigned to a user 
 * to be completed with title, description, priority, due date and status.
 * 
 * @author Gonzalo Qu3dena
 * @since 1.0.0
 */
@Getter
@Entity
public class Task extends AuditableAbstractAggregateRoot<Task> {

  /**
   * The title of the task
   */
  @NotBlank
  @Size(min = 3, max = 80)
  @Column(nullable = false, length = 80)
  private String title;

  /**
   * The description of the task
   */
  @Size(max = 250)
  @Column(length = 250)
  private String description;

  /**
   * The status of the task
   */
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TaskStatus status;

  /**
   * The priority of the task
   */
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TaskPriority priority;

  /**
   * The due date of the task
   */
  private LocalDate dueDate;

  /**
   * Default constructor for JPA
   */
  protected Task() {
    // JPA requires a no-args constructor
  }

  /**
   * Constructor of the task
   * @param title The title of the task
   * @param description The description of the task
   * @param priority The priority of the task
   * @param dueDate The due date of the task
   */
  public Task(String title, String description, TaskPriority priority, LocalDate dueDate) {
    this.title = title;
    this.description = description;
    this.status = TaskStatus.TODO;
    this.priority = priority;
    this.dueDate = dueDate;
  }

  /**
   * Factory method to create a new task
   * @param title The title of the task
   * @param description The description of the task
   * @param priority The priority of the task
   * @param dueDate The due date of the task
   * @return The new task
   */
  public Task create(String title, String description, TaskPriority priority, LocalDate dueDate) {
    this.title = title;
    this.description = description;
    this.status = TaskStatus.TODO;
    this.priority = priority;
    this.dueDate = dueDate;
    return new Task(title, description, priority, dueDate);
  }

  /**
   * Mark the task as done
   * @return true if the task is marked as done, false otherwise
   */
  public boolean markAsDone() {
    if (this.isOverdue()) {
      return false;
    }

    this.status = TaskStatus.DONE;
    return true;
  }

  /**
   * Mark the task as in progress
   */
  public void markAsInProgress() {
    this.status = TaskStatus.IN_PROGRESS;
  }

  /**
   * Check if the task is overdue
   * <p>
   * A task is overdue if the due date is before the current date and the status is not done.
   * </p>
   * @return true if the task is overdue, false otherwise
   */
  public boolean isOverdue() {
    if (this.dueDate == null) {
      return false;
    }

    return (LocalDate.now().isAfter(this.dueDate) && !TaskStatus.DONE.equals(this.status));
  }

  /**
   * Update task fields
   * @param title The title of the task
   * @param description The description of the task
   * @param priority The priority of the task
   * @param dueDate The due date of the task
   * @param status The status of the task
   */
  public void update(String title, String description, TaskPriority priority, LocalDate dueDate, TaskStatus status) {
    this.title = title;
    this.description = description;
    this.priority = priority;
    this.dueDate = dueDate;
    this.status = status;
  }

  /**
   * Update task status
   * @param status The new status
   */
  public void updateStatus(TaskStatus status) {
    this.status = status;
  }
}
