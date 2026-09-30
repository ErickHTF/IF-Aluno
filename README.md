# IF-Aluno

Backend (Spring Boot) + frontend (JSF/PrimeFaces).

| Serviço | Porta |
|---|---|
| Postgres (Docker) | 5433 |
| Backend | 8081 |
| Frontend (Tomcat 9) | 8080 |

## Rodar

```bash
# 1. banco (raiz)
docker compose up -d

# 2. backend (raiz) - ou botão Run em DemoApplication no IntelliJ
mvnw.cmd spring-boot:run

# 3. frontend (frontend/if-aluno-jsf)
mvnw.cmd clean package cargo:run
```

- API/Swagger: http://localhost:8081/swagger-ui/index.html
- Front: http://localhost:8080/if-aluno-jsf/

Front consome `http://localhost:8081/demo-api` (`ifaluno.utils.JsonUtils`).
