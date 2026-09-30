# Technology Stack

## Overview
| Layer | Technology | Approach |
|---|---|---|
| Frontend | React (with Vite) | Single-page application organised into `pages`, `components` and `services` |
| Backend | Java 21 + Spring Boot | REST API exchanging JSON |
| Persistence | MySQL + Spring Data JPA (Hibernate) | Relational database accessed through repositories |
| Build/dependencies | Maven (backend), npm (frontend) | One build tool per application |
| Infrastructure | Docker Compose | Runs the database locally in the same way for every team member |

## Justification

### Frontend: React
- **Suitability:** well suited for interactive interfaces such as student profiles and group views.
- **Maintainability:** component-based structure keeps UI code small and reusable; the `services` layer isolates API calls from the UI.
- **Testability:** supported by Vitest and React Testing Library.
- **Integration:** consumes the backend REST API through standard HTTP requests.
- **Team knowledge:** chosen as a current, widely adopted technology that the team wants to learn and use; the learning curve is mitigated by its large community and documentation.
- **Tooling:** large ecosystem, extensive documentation, fast development server.

### Backend: Java + Spring Boot (REST API)
- **Constraint:** Java is mandatory for the backend in this course.
- **Suitability:** mature framework for business applications with a clear layered structure (controllers, services, domain, repositories).
- **Maintainability:** dependency injection and layered architecture keep business rules separate from the API and persistence.
- **Testability:** strong testing ecosystem (JUnit 5, Mockito, Spring Boot Test), important for a Software Quality project.
- **Integration:** REST with JSON is simple to consume from React and easy to test with tools such as Postman.
- **Team knowledge:** the team has already used Spring Boot in previous projects, which reduces setup risk.
- **Tooling:** good IDE support (IntelliJ IDEA, VS Code) and Spring Initializr for project setup.

### Persistence: MySQL + Spring Data JPA (Hibernate)
- **Suitability:** StudyMatch data is highly relational (students, course units, enrolments, grades, competencies), which fits a relational database.
- **Maintainability:** Hibernate (JPA) maps domain entities to tables and reduces repetitive SQL code.
- **Testability:** repositories can be tested in isolation; integration tests can use a real MySQL instance (e.g. Testcontainers).
- **Integration:** Spring Data JPA uses Hibernate as its default implementation, natively supported by Spring Boot with the MySQL connector.
- **Team knowledge:** the team has already used Hibernate and MySQL in previous projects.
- **Tooling:** official Docker image available, plus graphical tools such as MySQL Workbench or DBeaver.

### Build: Maven and npm
- **Suitability:** standard build tools for the Java and JavaScript ecosystems.
- **Maintainability:** dependencies and versions are declared in `pom.xml` and `package.json`.
- **Testability:** both run automated tests from the command line (`mvn test`, `npm test`), which later allows CI integration.
- **Integration:** directly supported by Spring Boot.
- **Team knowledge:** Maven is the default build tool for Spring Boot projects, already used by the team in previous projects.
- **Tooling:** well supported by IDEs and GitHub Actions.

## Decision Status
This decision was agreed by the team during Sprint 1 and may be revised if new requirements justify it.
