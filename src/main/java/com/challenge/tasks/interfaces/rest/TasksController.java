package com.challenge.tasks.interfaces.rest;

import com.challenge.tasks.application.internal.commandservices.TaskCommandServiceImpl;
import com.challenge.tasks.application.internal.queryservices.TaskQueryServiceImpl;
import com.challenge.tasks.domain.model.commands.DeleteTaskCommand;
import com.challenge.tasks.domain.exceptions.TaskNotFoundException;
import com.challenge.tasks.domain.model.queries.GetAllTasksQuery;
import com.challenge.tasks.domain.model.queries.GetTaskByIdQuery;
import com.challenge.tasks.domain.model.queries.GetTaskStatsQuery;
import com.challenge.tasks.domain.model.valueobjects.TaskStatus;
import com.challenge.tasks.domain.model.valueobjects.TaskPriority;
import com.challenge.tasks.interfaces.rest.resources.CreateTaskResource;
import com.challenge.tasks.interfaces.rest.resources.TaskResource;
import com.challenge.tasks.interfaces.rest.resources.TaskStatsResource;
import com.challenge.tasks.interfaces.rest.resources.UpdateTaskResource;
import com.challenge.tasks.interfaces.rest.resources.UpdateTaskStatusResource;
import com.challenge.tasks.interfaces.rest.transform.CreateTaskCommandFromResourceAssembler;
import com.challenge.tasks.interfaces.rest.transform.UpdateTaskCommandFromResourceAssembler;
import com.challenge.tasks.interfaces.rest.transform.UpdateTaskStatusCommandFromResourceAssembler;
import com.challenge.tasks.interfaces.rest.transform.TaskResourceFromEntityAssembler;
import com.challenge.tasks.interfaces.rest.transform.TaskStatsResourceFromStatsAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Task management
 */
@RestController
@RequestMapping(value = "/api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tasks", description = "Available Task Endpoints")
public class TasksController {

  private final TaskCommandServiceImpl commandService;
  private final TaskQueryServiceImpl queryService;

  public TasksController(TaskCommandServiceImpl commandService, TaskQueryServiceImpl queryService) {
    this.commandService = commandService;
    this.queryService = queryService;
  }

  /**
   * Create a new task
   * 
   * @param resource The resource to create the task
   * @return The created task
   */
  @Operation(summary = "Create a new task", description = "Creates a new task. If status is not sent, defaults to TODO")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Task created successfully",
          content = @Content(schema = @Schema(implementation = TaskResource.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input data")
  })
  @PostMapping
  public ResponseEntity<TaskResource> createTask(@Valid @RequestBody CreateTaskResource resource) {
    var command = CreateTaskCommandFromResourceAssembler.toCommandFromResource(resource);
    var createdTask = commandService.handle(command);
    
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(TaskResourceFromEntityAssembler.toResourceFromEntity(createdTask));
  }

  /**
   * List tasks with optional filters
   * @param status The status to filter by
   * @param priority The priority to filter by
   * @param q The search term
   * @return The list of tasks
   */
  @Operation(summary = "List tasks", description = "List tasks with optional filters: status, priority, and search query")
  @ApiResponse(responseCode = "200", description = "List of tasks",
      content = @Content(schema = @Schema(implementation = TaskResource.class)))
  @GetMapping
  public ResponseEntity<List<TaskResource>> getAllTasks(
      @Parameter(description = "Filter by status") @RequestParam(required = false) TaskStatus status,
      @Parameter(description = "Filter by priority") @RequestParam(required = false) TaskPriority priority,
      @Parameter(description = "Search term for title and description") @RequestParam(required = false) String q) {
    
    var query = new GetAllTasksQuery(status, priority, q);
    var tasks = queryService.handle(query).stream()
        .map(TaskResourceFromEntityAssembler::toResourceFromEntity)
        .collect(Collectors.toList());
    
    return ResponseEntity.ok(tasks);
  }

  /**
   * Get a task by ID
   * @param id The ID of the task
   * @return The task
   */
  @Operation(summary = "Get task by ID", description = "Retrieves a task by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task found",
          content = @Content(schema = @Schema(implementation = TaskResource.class))),
      @ApiResponse(responseCode = "404", description = "Task not found")
  })
  @GetMapping("/{id}")
  public ResponseEntity<TaskResource> getTaskById(
      @Parameter(description = "Task ID") @PathVariable Long id) {
    var query = new GetTaskByIdQuery(id);
    return queryService.handle(query)
        .map(TaskResourceFromEntityAssembler::toResourceFromEntity)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new TaskNotFoundException(id));
  }

  /**
   * Update a task completely (except id/createdAt)
   * @param id The ID of the task
   * @param resource The resource to update the task
   * @return The updated task
   */
  @Operation(summary = "Update task", description = "Completely updates a task (except id/createdAt)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task updated successfully",
          content = @Content(schema = @Schema(implementation = TaskResource.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input data"),
      @ApiResponse(responseCode = "404", description = "Task not found")
  })
  @PutMapping("/{id}")
  public ResponseEntity<TaskResource> updateTask(
      @Parameter(description = "Task ID") @PathVariable Long id,
      @Valid @RequestBody UpdateTaskResource resource) {
    
    var command = UpdateTaskCommandFromResourceAssembler.toCommandFromResource(resource, id);
    commandService.handle(command);
    
    var query = new GetTaskByIdQuery(id);
    return queryService.handle(query)
        .map(TaskResourceFromEntityAssembler::toResourceFromEntity)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new TaskNotFoundException(id));
  }

  /**
   * Update only the status of a task
   * @param id The ID of the task
   * @param resource The resource to update the task status
   * @return The updated task
   */
  @Operation(summary = "Update task status", description = "Updates only the status of a task")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Status updated successfully",
          content = @Content(schema = @Schema(implementation = TaskResource.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input data"),
      @ApiResponse(responseCode = "404", description = "Task not found"),
      @ApiResponse(responseCode = "409", description = "Conflict - Cannot mark overdue task as done")
  })
  @PatchMapping("/{id}/status")
  public ResponseEntity<TaskResource> updateTaskStatus(
      @Parameter(description = "Task ID") @PathVariable Long id,
      @Valid @RequestBody UpdateTaskStatusResource resource) {
    
    var command = UpdateTaskStatusCommandFromResourceAssembler.toCommandFromResource(resource, id);
    commandService.handle(command);
    
    var query = new GetTaskByIdQuery(id);
    return queryService.handle(query)
        .map(TaskResourceFromEntityAssembler::toResourceFromEntity)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new TaskNotFoundException(id));
  }

  /**
   * Delete a task
   * @param id The ID of the task
   * @return The deleted task
   */
  @Operation(summary = "Delete task", description = "Deletes a task by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Task not found")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(
      @Parameter(description = "Task ID") @PathVariable Long id) {
    var command = new DeleteTaskCommand(id);
    commandService.handle(command);
    return ResponseEntity.noContent().build();
  }

  /**
   * Get task statistics
   * @return The task statistics
   */
  @Operation(summary = "Get task statistics", description = "Retrieves aggregated task statistics using Java Streams")
  @ApiResponse(responseCode = "200", description = "Task statistics",
      content = @Content(schema = @Schema(implementation = TaskStatsResource.class)))
  @GetMapping("/stats")
  public ResponseEntity<TaskStatsResource> getTaskStats() {
    var query = new GetTaskStatsQuery();
    var stats = queryService.handle(query);
    var statsResource = TaskStatsResourceFromStatsAssembler.toResourceFromStats(stats);
    return ResponseEntity.ok(statsResource);
  }
}
