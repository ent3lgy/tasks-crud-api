package com.challenge.tasks.application.internal.queryservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.challenge.tasks.domain.services.TaskQueryService;
import com.challenge.tasks.domain.model.aggregates.Task;
import com.challenge.tasks.domain.model.queries.GetAllTasksQuery;
import com.challenge.tasks.domain.model.queries.GetTaskByIdQuery;
import com.challenge.tasks.domain.model.queries.GetTaskStatsQuery;
import com.challenge.tasks.domain.model.valueobjects.TaskStatus;
import com.challenge.tasks.infrastructure.persistence.jpa.repositories.TaskRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the Task Query Service
 */
@Service
@Transactional(readOnly = true)
public class TaskQueryServiceImpl implements TaskQueryService {

  private final TaskRepository taskRepository;

  /**
   * Constructor of the service
   * @param taskRepository The repository to use
   */
  public TaskQueryServiceImpl(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  // {@inheritDoc}
  @Override
  public List<Task> handle(GetAllTasksQuery query) {
    var search = query.search();
    var status = query.status();
    var priority = query.priority();

    // Verify by status, priority and search term
    if (status != null && priority != null && search != null && !search.trim().isEmpty()) {
      return taskRepository.findByStatusAndPriorityAndSearchTerm(status, priority, search.trim());
    } 
    // Verify by status and search term
    else if (status != null && search != null && !search.trim().isEmpty()) {
      return taskRepository.findByStatusAndSearchTerm(status, search.trim());
    } 
    // Verify by priority and search term
    else if (priority != null && search != null && !search.trim().isEmpty()) {
      return taskRepository.findByPriorityAndSearchTerm(priority, search.trim());
    } 
    // Verify by status and priority
    else if (status != null && priority != null) {
      return taskRepository.findByStatusAndPriority(status, priority);
    } 
    // Verify by status
    else if (status != null) {
      return taskRepository.findByStatus(status);
    } 
    // Verify by priority
    else if (priority != null) {
      return taskRepository.findByPriority(priority);
    } 
    // Verify by search term
    else if (search != null && !search.trim().isEmpty()) {
      return taskRepository.findBySearchTerm(search.trim());
    } 
    // Return all tasks
    else {
      return taskRepository.findAll();
    }
  }

  // {@inheritDoc}
  @Override
  public Optional<Task> handle(GetTaskByIdQuery query) {
    return taskRepository.findById(query.taskId());
  }

  // {@inheritDoc}
  @Override
  public Map<String, Object> handle(GetTaskStatsQuery query) {
    var allTasks = taskRepository.findAll();

    var total = allTasks.stream().count();
    
    var byStatus = allTasks.stream()
        .collect(Collectors.groupingBy(
            task -> task.getStatus().name(), 
            Collectors.counting()
        ));
    
    var byPriority = allTasks.stream()
        .collect(Collectors.groupingBy(
            task -> task.getPriority().name(), 
            Collectors.counting()
        ));
    
    var overdue = allTasks.stream()
        .filter(Task::isOverdue)
        .count();
    
    var today = LocalDate.now();
    var sevenDaysFromNow = today.plusDays(7);
    
    var next7Days = allTasks.stream()
        .filter(task -> task.getDueDate() != null)
        .filter(task -> !task.getDueDate().isBefore(today))
        .filter(task -> !task.getDueDate().isAfter(sevenDaysFromNow))
        .filter(task -> !TaskStatus.DONE.equals(task.getStatus()))
        .sorted(Comparator.comparing(Task::getDueDate))
        .limit(5)
        .collect(Collectors.toList());

    return Map.of(
        "total", total,
        "byStatus", byStatus,
        "byPriority", byPriority,
        "overdue", overdue,
        "next7Days", next7Days
    );
  }
}
