# StudyMatch

Adaptive student group formation — Software Quality Project.

StudyMatch is a full-stack application that supports the automatic formation of student groups based on their academic history and competencies.

## Team

| Name | Student number |
|---|---|
| Rodrigo Miguel | 51856 |
| José Pinto | 51989 |
| Pedro Nobre | 51656 |
| Hugo Pereira | 51721 |

## Tech Stack

- **Frontend:** React (Vite)
- **Backend:** Java, Spring Boot (REST API)
- **Database:** MySQL, Spring Data JPA / Hibernate
- **Build:** Maven, npm

See [docs/tech-stack.md](docs/tech-stack.md) for the justification.

## Project status

Sprint 2 integrates account access, student and teacher management, study-plan management, academic results, student profiles and cold start. Group formation and teacher assignment to course units remain outside this implementation.

## Documentation

- [Workflow](docs/workflow.md)
- [Technology stack](docs/tech-stack.md)
- [Use cases and implementation scope](docs/use-cases/README.md)
- [Domain model](docs/domain-model.md)
- [Architecture](docs/architecture.md)
- [Coding conventions](docs/coding-conventions.md)
- [Definition of Done](docs/definition-of-done.md)

## Requirements

Git, JDK 21 with a valid JAVA_HOME, Node.js with npm, Docker Desktop with Compose, and network access for the initial dependency downloads. The Maven wrapper is included. On Windows, use npm.cmd if PowerShell blocks npm.ps1.

## Start the application

From the repository root, start MySQL:

```powershell
docker compose up -d
docker compose ps
```

Wait for mysqldb to become healthy. The included configuration uses port 3308, database studymatch_revisao_db and the mysql_revisao_data volume. Local development credentials are configured in docker-compose.yml. Do not remove an existing volume to perform an ordinary restart.

In a separate terminal, start the backend:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

In another terminal, start the frontend:

```powershell
cd frontend
npm.cmd ci
npm.cmd run dev
```

Open http://localhost:5173. The backend is available at http://localhost:8080/api. The study plan and three default accounts are initialized; sample academic histories are not automatically created. Existing account passwords are not reset on startup.

| Role | Number | Initial local password |
|---|---|---|
| Academic administrator | 12345 | admin |
| Teacher | 23456 | teacher |
| Student | 34567 | students |

Sign in with the number or number@studymatch.com. Newly registered students and teachers share a persistent sequence starting at 1; their generated initial password is returned only at registration. Treat these default credentials as development data, not deployment credentials.

## Tests and builds

From backend:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

Spring integration tests activate the `test` profile and load `backend/src/test/resources/application-test.yml`, which uses an in-memory H2 database and does not require MySQL. The normal `spring-boot:run` command uses the default profile and MySQL settings from `backend/src/main/resources/application.yml`. A successful H2 test run does not substitute for checking the normal MySQL configuration.

From frontend, after installing dependencies:

```powershell
npm.cmd run build
```

The outputs are backend/target/ and frontend/dist/. On Linux/macOS use ./mvnw and npm; ensure backend/mvnw is executable if needed.

## Configuration

| Variable | Default / meaning |
|---|---|
| DB_PORT | 3308; changing this does not change the Compose port mapping |
| DB_URL | Full MySQL JDBC URL, overriding the default URL |
| DB_USERNAME / DB_PASSWORD | Local database account from docker-compose.yml |
| SERVER_PORT | 8080 |
| FRONTEND_ORIGIN | http://localhost:5173; must match the browser origin |
| SESSION_COOKIE_SECURE | false for local HTTP; session cookie secure attribute |
| VITE_API_URL | http://localhost:8080/api; frontend build/dev setting |

Set backend variables in its terminal before starting Maven. Frontend variables may be set in frontend/.env.local; restart Vite after a change. Do not commit private environment files.

## Manual verification

1. Sign in as administrator and register a student and a teacher. Search by their generated number and edit their data.
2. Enrol the active student in course units, record results and open the student profile. Change a grade and reload the profile to observe recalculation.
3. Compare a student with results, one with only an entry grade and one without either source. These scenarios require preparing data manually.
4. Check the area detail, password change, account access state and ownership restrictions for photos.
5. Restart the backend with the same MySQL volume and confirm persistence.

The review must report the checks actually performed. These instructions do not claim that a clean-clone check, browser review or MySQL verification has already been completed by the team.

## Stop

Use Ctrl+C in the frontend/backend terminals and docker compose down at the root. The database volume is retained. If a port is occupied, identify the existing process/container before changing configuration or stopping it.
