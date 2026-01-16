package com.challenge.tasks.interfaces.rest;

import com.challenge.tasks.domain.model.valueobjects.TaskStatus;
import com.challenge.tasks.domain.model.valueobjects.TaskPriority;
import com.challenge.tasks.interfaces.rest.resources.CreateTaskResource;
import com.challenge.tasks.interfaces.rest.resources.UpdateTaskStatusResource;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Integration for TasksController
 * @summary
 * This class contains the integration tests for the TasksController. It tests the REST API endpoints for creating, updating and getting tasks.
 * @author Gonzalo Qu3dena
 * @version 1.0.0
 * @since 1.0.0
 */
@SpringBootTest
@AutoConfigureMockMvc
class TasksControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Should create task and return 201 when request is valid")
  void shouldCreateTaskAndReturn201WhenRequestIsValid() throws Exception {
    // Arrange - Create a task with valid data
    var createTaskResource = new CreateTaskResource(
        "Valid Task Title",
        "Valid task description",
        TaskPriority.MEDIUM,
        LocalDate.now().plusDays(5),
        null
    );

    // Act & Assert
    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createTaskResource)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.title").value("Valid Task Title"))
        .andExpect(jsonPath("$.priority").value("MEDIUM"))
        .andExpect(jsonPath("$.status").value("TODO"));
  }

  @Test
  @DisplayName("Should return 400 when priority is high and due date is null")
  void shouldReturn400WhenPriorityIsHighAndDueDateIsNull() throws Exception {
    // Arrange - Create a task with high priority and no due date
    var createTaskResource = new CreateTaskResource(
        "High Priority Task",
        "Task without due date",
        TaskPriority.HIGH,
        null,
        null
    );

    // Act & Assert
    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createTaskResource)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(containsString("due date is mandatory")));
  }

  @Test
  @DisplayName("Should return 409 when marking overdue task as done")
  void shouldReturn409WhenMarkingOverdueTaskAsDone() throws Exception {
    // Arrange - Create a task with past due date and status != DONE
    var createTaskResource = new CreateTaskResource(
        "Overdue Task",
        "Task with past due date",
        TaskPriority.MEDIUM,
        LocalDate.now().minusDays(5),
        TaskStatus.TODO
    );

    String response = mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createTaskResource)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Long taskId = objectMapper.readTree(response).get("id").asLong();

    var updateStatusResource = new UpdateTaskStatusResource(TaskStatus.DONE);

    // Act & Assert
    mockMvc.perform(patch("/api/tasks/{id}/status", taskId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateStatusResource)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.message").value(containsString("Cannot complete an overdue task")));
  }

  @Test
  @DisplayName("Should return task statistics and 200 when requested")
  void shouldReturnTaskStatisticsAnd200WhenRequested() throws Exception {
    // Arrange - Create test tasks with different statuses and priorities
    createTestTask("Task TODO LOW", TaskPriority.LOW, LocalDate.now().plusDays(1), TaskStatus.TODO);
    createTestTask("Task TODO MEDIUM", TaskPriority.MEDIUM, LocalDate.now().plusDays(2), TaskStatus.TODO);
    createTestTask("Task IN_PROGRESS HIGH", TaskPriority.HIGH, LocalDate.now().plusDays(3), TaskStatus.IN_PROGRESS);
    createTestTask("Task DONE MEDIUM", TaskPriority.MEDIUM, LocalDate.now().plusDays(4), TaskStatus.DONE);
    createTestTask("Task Overdue", TaskPriority.LOW, LocalDate.now().minusDays(1), TaskStatus.TODO);
    createTestTask("Task Next 7 Days", TaskPriority.HIGH, LocalDate.now().plusDays(5), TaskStatus.TODO);

    // Act & Assert
    mockMvc.perform(get("/api/tasks/stats"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.total").isNumber())
        .andExpect(jsonPath("$.byStatus").exists())
        .andExpect(jsonPath("$.byStatus.TODO").exists())
        .andExpect(jsonPath("$.byStatus.IN_PROGRESS").exists())
        .andExpect(jsonPath("$.byStatus.DONE").exists())
        .andExpect(jsonPath("$.byPriority").exists())
        .andExpect(jsonPath("$.byPriority.LOW").exists())
        .andExpect(jsonPath("$.byPriority.MEDIUM").exists())
        .andExpect(jsonPath("$.byPriority.HIGH").exists())
        .andExpect(jsonPath("$.overdue").isNumber())
        .andExpect(jsonPath("$.next7Days").isArray())
        .andExpect(jsonPath("$.next7Days", hasSize(lessThanOrEqualTo(5))))
        .andExpect(jsonPath("$.next7Days[0].dueDate").exists());
  }

  /**
   * Helper method to create test tasks
   */
  private void createTestTask(String title, TaskPriority priority, LocalDate dueDate, TaskStatus status) throws Exception {
    var createTaskResource = new CreateTaskResource(
        title,
        "New test task description",
        priority,
        dueDate,
        status
    );

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createTaskResource)))
        .andExpect(status().isCreated());
  }
}
