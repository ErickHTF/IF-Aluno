# IF-Aluno

CRUD for **students** (alunos) and **schools** (escolas) built for the Programming Language 2 course (IFSP).
A Spring Boot REST API stores the data in PostgreSQL. A JSF/PrimeFaces frontend consumes that API.

## Features

- **Students**: create, list, filter, edit and delete.
  - Fields: name, email, enrollment number, CPF, height, phone, date of birth and active.
  - Validation: name with 3 to 100 characters, valid email, CPF in the `XXX.XXX.XXX-XX` format, height greater than zero, phone like `(16) 99123-4567` and date of birth in the past. All fields are required.
  - Email and enrollment number are unique. A duplicate value returns `409 Conflict`.
- **Schools**: create, list, filter, edit and delete.
  - Fields: name (3 to 100 characters) and level (`FUNDAMENTAL`, `MEDIO` or `SUPERIOR`).
- **Filters**: text fields are matched by substring, case-insensitive. An omitted filter is ignored.
- **Errors**: validation failures and malformed data return `400` with a standard JSON body (`mensagem`, `mensagens` and `campos`, which holds the error for each field).

## Architecture

```
Browser ──► JSF Frontend (Tomcat 9, :8080) ──HTTP/JSON──► Spring Boot API (:8081) ──JPA──► PostgreSQL (Docker, :5433)
```

| Folder | Contents |
|---|---|
| `/` (root) | REST API, `com.demo` package, with `aluno`, `escola` and `config` modules |
| `frontend/if-aluno-jsf` | JSF/PrimeFaces frontend, packaged as a WAR |

The frontend calls the API at `http://localhost:8081/demo-api`. That URL is set in `ifaluno.utils.JsonUtils.BASE_API`.

## Requirements

| Item | Version |
|---|---|
| JDK | 21 (the API uses Java 21; the frontend compiles to Java 17) |
| Docker / Docker Compose | any recent version (to run PostgreSQL 13.22) |
| Maven | no install needed: use the `mvnw` wrapper (Maven 3.9.16) |

Main dependencies: Spring Boot 4.1.0, springdoc-openapi 2.8.6, JSF (Mojarra) 2.4.0, PrimeFaces 14.0.0, Weld 3.1.9 and Tomcat 9.0.98, which Cargo downloads on its own.

## Getting Started

Run the steps in order, each in its own terminal.

**Linux / macOS**

```bash
# 1. Database (from the root)
docker compose up -d

# 2. API (from the root)
./mvnw spring-boot:run

# 3. Frontend
cd frontend/if-aluno-jsf
./mvnw clean package cargo:run
```

**Windows (cmd / PowerShell)**

```bat
:: 1. Database (from the root)
docker compose up -d

:: 2. API (from the root)
mvnw.cmd spring-boot:run

:: 3. Frontend
cd frontend\if-aluno-jsf
mvnw.cmd clean package cargo:run
```

In IntelliJ, you can also start the API with the Run button on `DemoApplication`.

Tables are created automatically on first run (`ddl-auto=update`).

## Ports and URLs

| Service | Port | URL |
|---|---|---|
| PostgreSQL (Docker) | 5433 | `jdbc:postgresql://localhost:5433/Aluno` (user `aluno_user`, password `aluno_password`) |
| API (Spring Boot) | 8081 | http://localhost:8081/demo-api |
| Swagger UI | 8081 | http://localhost:8081/swagger-ui/index.html |
| Frontend (Tomcat 9) | 8080 | http://localhost:8080/if-aluno-jsf/ |

## API Endpoints

All endpoints live under the `/demo-api` prefix.

| Method | Route | Description |
|---|---|---|
| GET | `/alunos` | Lists all students |
| GET | `/alunos/{id}` | Gets a student by id (`404` if not found) |
| GET | `/aluno?id=&nome=&email=&matricula=` | Filters students (all parameters are optional) |
| POST | `/alunos` | Creates a student (`201`) |
| PUT | `/alunos/{id}` | Updates a student (`404` if not found) |
| DELETE | `/alunos/{id}` | Deletes a student |
| GET | `/escolas` | Lists all schools |
| GET | `/escolas/{id}` | Gets a school by id (`404` if not found) |
| GET | `/escola?id=&nome=&nivel=` | Filters schools (all parameters are optional) |
| POST | `/escolas` | Creates a school (`201`) |
| PUT | `/escolas/{id}` | Updates a school (`404` if not found) |
| DELETE | `/escolas/{id}` | Deletes a school |

Example body for `POST /demo-api/alunos`:

```json
{
  "nome": "João Silva",
  "email": "email@aluno.ifsp.edu.br",
  "matricula": "SP123456",
  "cpf": "358.823.769-41",
  "altura": 1.75,
  "telefone": "(16) 99123-4567",
  "dtNasc": "2005-03-15",
  "ativo": true
}
```

The Postman collections are in `src/main/resources/`.

## Build and Tests

```bash
./mvnw verify                                  # API (the test needs Postgres running)
cd frontend/if-aluno-jsf && ./mvnw package     # produces target/if-aluno-jsf.war
```

GitHub Actions (`.github/workflows/ci.yml`) runs both builds on every push to `main` and on every pull request. In CI, PostgreSQL runs as a service container.
