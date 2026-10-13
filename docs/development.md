# Development guide

[← Back to README](../README.md)

## Requirements

| Item                    | Version                                                     |
| ----------------------- | ----------------------------------------------------------- |
| JDK                     | 21 (the API uses Java 21; the frontend compiles to Java 17) |
| Docker / Docker Compose | any recent version (to run PostgreSQL 13.22)                |
| Maven                   | no install needed: use the `mvnw` wrapper (Maven 3.9.16)    |

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

| Service             | Port | URL                                                                                     |
| ------------------- | ---- | --------------------------------------------------------------------------------------- |
| PostgreSQL (Docker) | 5433 | `jdbc:postgresql://localhost:5433/Aluno` (user `aluno_user`, password `aluno_password`) |
| API (Spring Boot)   | 8081 | http://localhost:8081/demo-api                                                          |
| Swagger UI          | 8081 | http://localhost:8081/swagger-ui/index.html                                             |
| Frontend (Tomcat 9) | 8080 | http://localhost:8080/if-aluno-jsf/                                                     |

## Build and Tests

```bash
./mvnw verify                                  # API (the test needs Postgres running)
cd frontend/if-aluno-jsf && ./mvnw package     # produces target/if-aluno-jsf.war
```

GitHub Actions (`.github/workflows/ci.yml`) runs both builds on every push to `main` and on every pull request. In CI, PostgreSQL runs as a service container.
