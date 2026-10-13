# Architecture

[← Back to README](../README.md)

## Architecture

```
Browser ──► JSF Frontend (Tomcat 9, :8080) ──HTTP/JSON──► Spring Boot API (:8081) ──JPA──► PostgreSQL (Docker, :5433)
```

| Folder                  | Contents                                                                  |
| ----------------------- | ------------------------------------------------------------------------- |
| `/` (root)              | REST API, `com.demo` package, with `aluno`, `escola` and `config` modules |
| `frontend/if-aluno-jsf` | JSF/PrimeFaces frontend, packaged as a WAR                                |

The frontend calls the API at `http://localhost:8081/demo-api`. That URL is set in `ifaluno.utils.JsonUtils.BASE_API`.
