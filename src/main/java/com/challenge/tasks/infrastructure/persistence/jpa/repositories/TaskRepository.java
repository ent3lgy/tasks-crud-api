package com.challenge.tasks.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge.tasks.domain.model.aggregates.Task;
import com.challenge.tasks.domain.model.valueobjects.TaskStatus;
import com.challenge.tasks.domain.model.valueobjects.TaskPriority;

import java.util.List;

/**
 * Repository for tasks
 * @summary
 * This repository is responsible for the persistence of tasks. It provides methods to find tasks by status, priority, search term and more.
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  
  /**
   * Find tasks by status
   * @param status The status to filter by
   * @return List of tasks with the specified status
   */
  List<Task> findByStatus(TaskStatus status);

  /**
   * Find tasks by priority
   * @param priority The priority to filter by
   * @return List of tasks with the specified priority
   */
  List<Task> findByPriority(TaskPriority priority);

  /**
   * Find tasks by status and priority
   * @param status The status to filter by
   * @param priority The priority to filter by
   * @return List of tasks matching both criteria
   */
  List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);

  /**
   * Find tasks by search term (searches in title and description)
   * @param searchTerm The search term
   * @return List of tasks matching the search term
   */
  @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
  List<Task> findBySearchTerm(@Param("searchTerm") String searchTerm);

  /**
   * Find tasks by status and search term
   * @param status The status to filter by
   * @param searchTerm The search term
   * @return List of tasks matching both criteria
   */
  @Query("SELECT t FROM Task t WHERE t.status = :status AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
  List<Task> findByStatusAndSearchTerm(@Param("status") TaskStatus status, @Param("searchTerm") String searchTerm);

  /**
   * Find tasks by priority and search term
   * @param priority The priority to filter by
   * @param searchTerm The search term
   * @return List of tasks matching both criteria
   */
  @Query("SELECT t FROM Task t WHERE t.priority = :priority AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
  List<Task> findByPriorityAndSearchTerm(@Param("priority") TaskPriority priority, @Param("searchTerm") String searchTerm);

  /**
   * Find tasks by status, priority and search term
   * @param status The status to filter by
   * @param priority The priority to filter by
   * @param searchTerm The search term
   * @return List of tasks matching all criteria
   */
  @Query("SELECT t FROM Task t WHERE t.status = :status AND t.priority = :priority AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
  List<Task> findByStatusAndPriorityAndSearchTerm(@Param("status") TaskStatus status, @Param("priority") TaskPriority priority, @Param("searchTerm") String searchTerm);
}
