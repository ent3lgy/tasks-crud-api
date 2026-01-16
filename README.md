# Tasks CRUD API - Technical Challenge

## Summary

<p align="justify">
REST API for high-performance task management built with <b>Java 17</b> and <b>Spring Boot 3.5</b>. The project follows <b>Domain-Driven Design (DDD)</b> principles and a layered architecture to ensure maintainability and scalability.
</p>

## Features

- **Full CRUD**: Create, Read, Update, and Delete tasks.
- **Advanced Filtering**: Search by title/description and filter by status or priority.
- **Intelligent Statistics**: Aggregated data using Java Streams (O(n) complexity).
- **Business Rules Enforcement**: Validation at both API and Service layers.
- **Automatic Auditing**: Auto-generated `createdAt` and `updatedAt` timestamps.
- **Interactive Documentation**: Swagger UI for real-time API testing.

---

## Requirements

- **Java**: 17+ (LTS recommended)
- **Maven**: 3.x
- **Docker**: Optional (for containerized execution)

## Installation & Execution

### Local Execution

1. **Clone the repository**
   <p align="justify">Download the source code to your local environment.</p>

   ```bash
   git clone https://github.com/entelgy/tasks-crud.git
   ```

2. **Build and run tests**
   <p align="justify">Verify that all business rules and unit tests pass correctly using the Maven Wrapper.</p>

   ```bash
   ./mvnw clean test
   ```

3. **Run the application**
   <p align="justify">Launch the Spring Boot application using the embedded Tomcat server.</p>

   ```bash
   ./mvnw spring-boot:run
   ```

### Docker Execution

1. **Package the application**
   <p align="justify">Generate the executable JAR artifact in the <code>target</code> folder.</p>

   ```bash
   ./mvnw package -DskipTests
   ```

2. **Build the Docker image**
   <p align="justify">Create a container image based on the <code>Dockerfile</code>, which includes the JAR and the JRE 17 environment.</p>

   ```bash
   docker build -t tasks-crud-app:1.0 .
   ```

3. **Run the container**
   <p align="justify">Start the containerized application mapping the internal port to your local machine.</p>

   ```bash
   docker run -p 8080:8080 tasks-crud-app:1.0
   ```

---

## Database & Monitoring

### H2 In-Memory Console

Useful for real-time data inspection without external tools.

- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:tasksdb`
- **User**: `sa`
- **Password**: (leave blank)

### API Documentation (OpenAPI/Swagger)

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

---

## Business Rules & Error Handling

<table>
  <thead>
    <tr>
      <th>Code</th>
      <th>Type</th>
      <th>Logic / Rule</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><b>400</b></td>
      <td><b>Bad Request</b></td>
      <td><b>Rule B</b>: For <code>priority=HIGH</code>, <code>dueDate</code> is mandatory. Also triggered by invalid field lengths.</td>
    </tr>
    <tr>
      <td><b>404</b></td>
      <td><b>Not Found</b></td>
      <td>Triggered when a Task ID does not exist in the system.</td>
    </tr>
    <tr>
      <td><b>409</b></td>
      <td><b>Conflict</b></td>
      <td><b>Rule A</b>: Cannot mark a task as <code>DONE</code> if it is overdue (<code>dueDate</code> < today).</td>
    </tr>
  </tbody>
</table>

---

## Request Examples (cURL)

### 1. Create a Task (POST)

```bash
curl -i -X POST "http://localhost:8080/api/tasks" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete Technical Challenge",
    "description": "Improve README and verify business rules",
    "priority": "HIGH",
    "dueDate": "2026-01-20"
  }'
```

### 2. List Tasks with Filters (GET)

```bash
curl -s "http://localhost:8080/api/tasks?status=TODO&priority=HIGH&q=Technical"
```

### 3. Update Task Status (PATCH)

```bash
curl -i -X PATCH "http://localhost:8080/api/tasks/1/status" \
  -H "Content-Type: application/json" \
  -d '{ "status": "DONE" }'
```

### 4. Get Statistics (GET)

```bash
curl -s "http://localhost:8080/api/tasks/stats" | jq .
```

---

## Project Structure (Layered)

- **Domain**: Core business logic, entities (`Task`), and Value Objects (`TaskStatistics`).
- **Application**: Command and Query services handling use cases.
- **Infrastructure**: Persistence implementation (Spring Data JPA / H2).
- **Interfaces**: REST Controllers, DTOs (Resources), and Assemblers for mapping.

---

**Author**: Gonzalo Qu3dena
<br/>
**Version**: 1.0.0
