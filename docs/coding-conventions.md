# Coding Conventions

Basic conventions agreed by the team for Sprint 1. They follow the official Java and JavaScript conventions and the structure described in [architecture.md](architecture.md).

## General
- Code, comments, identifiers and commit messages are written in English.
- Text shown to the user in the interface is written in Portuguese.
- Use meaningful names and avoid abbreviations (`studentNumber`, not `sNum`).
- Keep methods and components small, with a single responsibility.
- No unused code, commented-out code or debug prints (`System.out.println`, `console.log`) in merged code.

## Backend (Java / Spring Boot)

### Naming
| Element | Convention | Example |
|---|---|---|
| Classes, interfaces, enums | PascalCase | `StudentService`, `StudentStatus` |
| Methods and variables | camelCase | `calculateScore`, `entryGrade` |
| Constants and enum values | UPPER_SNAKE_CASE | `MAX_ECTS_PER_YEAR`, `ACTIVE` |
| Packages | lowercase, no underscores | `pt.studymatch.service` |
| REST endpoints | lowercase, plural nouns, words separated by hyphens; all under the `/api` context path | `/api/knowledge-areas` |

### Class suffixes
| Suffix | Package | Responsibility |
|---|---|---|
| `Controller` | `controller` | REST endpoints and input format validation only. No business logic. |
| `DTO` | `dto` | Data received from or sent to the API (e.g. `KnowledgeAreaDTO`). Validation annotations live here. |
| `Mapper` | `mapper` | Conversion between entities and DTOs (`toDTO`, `toEntity`). |
| `Service` | `service` | Use-case logic and business rules that need stored data. |
| `Repository` | `repository` | Spring Data JPA interfaces. |
| (none) | `domain` | Entities, enums and the rules that belong to them. |

### Package structure

    pt.studymatch
    ├── config        (CORS; global exception handler when added)
    ├── controller
    ├── dto
    ├── mapper
    ├── service
    ├── domain        (entities and enums)
    └── repository

### Validation and errors
These rules apply as soon as an endpoint receives input (Sprint 1 only has read endpoints).

- Input format is checked with Bean Validation annotations on DTOs (`@NotBlank`, `@Email`, `@Min`, `@Max`) and `@Valid` in controllers. This requires adding `spring-boot-starter-validation` to the `pom.xml`.
- Business rules are checked in services and domain classes. When a rule fails, a `BusinessRuleException` is thrown with a clear message.
- A single `@RestControllerAdvice` in `config` turns exceptions into HTTP responses (400 for invalid input, 404 for not found, 409/422 for business rule violations).

### Other rules
- Use constructor injection, not field injection (`@Autowired` on fields).
- Controllers never return entities; they always return DTOs.
- Entities use `Long id` as identifier (implementation detail, not part of the domain model).

## Frontend (React)

### Naming
| Element | Convention | Example |
|---|---|---|
| Components and pages | PascalCase, one per file | `StudentProfile.jsx` |
| Functions and variables | camelCase | `fetchStudents` |
| Service files | camelCase | `api.js`; split by resource (e.g. `studentService.js`) when it grows |

### Folder structure

    src
    ├── App.jsx      (main screen in Sprint 1)
    ├── pages        (full screens, created as screens are added)
    ├── components   (reusable UI, created when needed)
    └── services     (all calls to the backend API, e.g. api.js)

- Pages and components never call `fetch` directly; they use the functions in `services`.
- The backend URL is read from an environment variable (`VITE_API_URL`), not written in the code.

## Formatting
- Indentation: 4 spaces in Java, 2 spaces in JavaScript.
- Use the IDE's default formatter before committing.
- One class per file in Java; one component per file in React.

## Tests
- Backend tests use JUnit 5 through `spring-boot-starter-test`, added to the `pom.xml` with the first test.
- Test classes end in `Test` and mirror the package of the class they test (`StudentServiceTest`).
- Test method names describe the behaviour: `shouldRejectGradeAbove20`.
