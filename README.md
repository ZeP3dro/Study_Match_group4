# Study_Match_group4

Adaptive student group formation — Software Quality Project.

StudyMatch supports the formation of student groups based on academic history
and competencies. Sprint 1 provides a minimal React frontend that lists
knowledge areas from a Spring Boot API backed by MySQL.

## Team

- Rodrigo Miguel — 51856
- José Pinto — 51989
- Pedro Nobre — 51656
- Hugo Pereira — 51721

## Prerequisites

- Git.
- Java JDK 21, with `JAVA_HOME` pointing to the JDK installation.
- Node.js 22 or 24, with npm.
- Docker Desktop running with Linux containers and Docker Compose v2.
- Internet access to download dependencies and the MySQL image on the first run.

Maven is downloaded by the included wrapper. npm installs the frontend
dependencies. MySQL runs in Docker, so a separate MySQL installation is unnecessary.

Check the installed tools:

```powershell
git --version
java -version
node --version
npm --version
docker compose version
```

## Local setup

The commands below use Windows PowerShell. On Linux or macOS, replace
`.\mvnw.cmd` with `./mvnw`.

### 1. Clone the repository

```powershell
git clone https://github.com/ZeP3dro/Study_Match_group4.git
cd Study_Match_group4
```

### 2. Start MySQL

From the repository root:

```powershell
docker compose up -d
docker compose ps
```

Wait until `mysqldb` is healthy before starting the backend. MySQL is available
on `localhost:3308`, with database `studymatch_db`, username `studymatch_user`
and password `studymatch_password`. These defaults are for local development.

### 3. Start the backend

In a terminal at the repository root:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

The API runs at `http://localhost:8080/api`. Hibernate creates or updates the
tables, and the backend inserts five default knowledge areas if the table is empty.

### 4. Start the frontend

Open another terminal at the repository root:

```powershell
cd frontend
npm ci
npm run dev
```

Open `http://localhost:5173`. The page should display the knowledge areas
returned by the backend.

## Environment variables

The default settings work with the included Docker Compose file. No environment
variables are required for the setup above.

| Variable | Default | Purpose |
|---|---|---|
| `DB_PORT` | `3308` | MySQL port used by the backend. |
| `DB_URL` | MySQL JDBC URL for `localhost:3308/studymatch_db` | Overrides the complete database URL, including the port. |
| `DB_USERNAME` | `studymatch_user` | Database username. |
| `DB_PASSWORD` | `studymatch_password` | Database password. |
| `SERVER_PORT` | `8080` | Backend HTTP port. |
| `VITE_API_URL` | `http://localhost:8080/api` | Backend URL used by the frontend. |

Set backend variables in the terminal before running Maven. For example, to use
a different backend port:

```powershell
$env:SERVER_PORT = "8081"
.\mvnw.cmd spring-boot:run
```

In `frontend/.env.local`, set the matching frontend URL and restart Vite:

```dotenv
VITE_API_URL=http://localhost:8081/api
```

On Linux or macOS, use `export SERVER_PORT=8081` instead. The backend reads
system environment variables; it does not automatically load a `.env` file.
Changing `DB_PORT` does not change the Docker port mapping: update
`docker-compose.yml` too if using the included database. Keep the frontend on
`http://localhost:5173`, which is the origin allowed by the backend CORS configuration.

## Verification and builds

There are no automated tests in this initial skeleton. Check the integration manually:

1. Open `http://localhost:8080/api/knowledge-areas`; expect a JSON list with five areas on a fresh database.
2. Open `http://localhost:5173`; expect the same names and descriptions on the page.
3. Restart the backend and confirm that the list is still available from MySQL.

Check that both applications build:

```powershell
# From backend/
.\mvnw.cmd clean package

# From frontend/, after npm ci
npm run build
```

The backend JAR is generated in `backend/target/`, and the frontend build in
`frontend/dist/`.

## Troubleshooting

| Problem | What to check |
|---|---|
| Docker cannot connect to its engine | Start Docker Desktop and wait until the engine is running. |
| MySQL is not healthy | Run `docker compose logs mysqldb` from the repository root. |
| Backend cannot connect to MySQL | Confirm the container is healthy and the database port and credentials match the backend settings. |
| Java compilation fails | Confirm `java -version` and `.\mvnw.cmd -version` use JDK 21; check `JAVA_HOME`. |
| A port is already in use | Stop the other process. For backend port changes, update `SERVER_PORT` and `VITE_API_URL` together. For MySQL, update the Compose port mapping and `DB_PORT`. |
| The page cannot load the areas | Check the backend endpoint, keep the page on `http://localhost:5173`, and restart Vite after changing its environment variables. |
| PowerShell blocks npm scripts | Use `npm.cmd ci` and `npm.cmd run dev`. |

## Stop the application

Press `Ctrl+C` in the frontend and backend terminals, then run from the repository root:

```powershell
docker compose down
```

The MySQL volume is retained, so database data persists between runs.

## Documentation

- [Technology stack](docs/tech-stack.md)
- [Architecture](docs/architecture.md)
- [Domain model](docs/domain-model.md)
- [Use cases](docs/use-cases/README.md)
- [Team workflow](docs/workflow.md)
