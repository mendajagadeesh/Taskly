# Taskly

Taskly is a secure task management application built with Spring Boot, JWT authentication, and MySQL.
It provides a REST API for user registration, login, and user-specific task operations, along with a modern static frontend for quick interaction.

## Highlights

- JWT-based authentication and authorization
- Role-aware access control with Spring Security
- User registration with encrypted passwords
- Task CRUD operations scoped by user ID
- Clean static frontend served by Spring Boot
- MySQL persistence with Spring Data JPA

## Tech Stack

- Java 25
- Spring Boot 4
- Spring Security
- Spring Data JPA
- MySQL
- JWT (jjwt)
- Maven
- HTML, CSS, JavaScript

## Project Structure

- Backend source: src/main/java/com/jagdev/Task_project
- Frontend static files: src/main/resources/static
- Config: src/main/resources/application.properties

## Prerequisites

- Java 25 installed
- Maven 3.9+ installed (or use Maven Wrapper included in repo)
- MySQL running locally

## Configuration

Update database configuration in src/main/resources/application.properties if needed:

- spring.datasource.url=jdbc:mysql://localhost:3306/task-project
- spring.datasource.username=root
- spring.datasource.password=root
- server.port=9000

Default behavior:

- Hibernate DDL: update
- SQL logging: enabled

## Run Locally

Using Maven Wrapper on Windows:

1. Open terminal in the project root
2. Run: mvnw.cmd spring-boot:run

Using Maven:

1. Open terminal in the project root
2. Run: mvn spring-boot:run

Application URLs:

- Frontend: http://localhost:9000
- API base: http://localhost:9000/api

## Authentication Flow

1. Register with name, email, and password
2. Login with email and password
3. Receive JWT token
4. Send token in Authorization header:

Authorization: Bearer YOUR_TOKEN

## API Endpoints

### Auth

- POST /api/auth/register
- POST /api/auth/login

Register request body:

{
	"name": "Jagadeesh",
	"email": "jag@example.com",
	"password": "yourPassword",
	"roles": ["ROLE_USER"]
}

Login request body:

{
	"email": "jag@example.com",
	"password": "yourPassword"
}

### Tasks

- POST /api/{userid}/tasks
- GET /api/{userid}/tasks
- GET /api/{userid}/tasks/{taskid}
- DELETE /api/{userid}/tasks/{taskid}

Create task request body:

{
	"taskName": "Ship backend API"
}

## Roles and Access

- New users default to ROLE_USER if roles are not provided during registration
- Task APIs require authenticated users
- Controller-level role checks are applied with @PreAuthorize

## Frontend

Taskly includes a static frontend at the root URL that supports:

- Registration
- Login
- Load all tasks for a user
- Add a task
- Find a task by ID
- Delete a task

The frontend stores JWT in browser local storage and sends it automatically with API requests.

## Notes

- JWT secret is currently hardcoded in the application code. For production, move it to environment variables or external secure config.
- Credentials in application.properties are local development defaults.

## Future Improvements

- Add update task endpoint (PUT/PATCH)
- Add global exception response format
- Add Swagger/OpenAPI documentation
- Add integration and security tests
- Add Docker and docker-compose setup

## License

This project is open for learning and personal development.