# if-aluno-jsf

Frontend JSF (javax) + PrimeFaces que consome a API REST do projeto **IF-Aluno**
(Spring Boot) em `http://localhost:8081/demo-api`.

Baseado no projeto de referência `demo-jsf`.

## Requisitos

- JDK 17
- Maven 3.9+
- Apache Tomcat 9 (a aplicação é empacotada como WAR)
- API IF-Aluno rodando em `http://localhost:8081/demo-api` (ver repositório IF-Aluno)

## Telas

- **Manter Aluno** (`/cadastros/ManterAlunoView.xhtml`): listar, filtrar, criar, editar e excluir alunos.
- **Manter Escola** (`/cadastros/ManterEscolaView.xhtml`): listar, filtrar, criar, editar e excluir escolas.

Os endpoints consumidos são:

| Recurso | Endpoint |
|---|---|
| Alunos | `GET/POST /demo-api/alunos`, `GET /demo-api/aluno`, `GET/PUT/DELETE /demo-api/alunos/{id}` |
| Escolas | `GET/POST /demo-api/escolas`, `GET /demo-api/escola`, `GET/PUT/DELETE /demo-api/escolas/{id}` |

## Build

```bash
mvn clean package
```

O WAR é gerado em `target/if-aluno-jsf.war`.

## Rodar via Maven (Cargo + Tomcat 9)

```bash
mvn clean package cargo:run
```

O Cargo baixa o Tomcat 9.0.98, sobe na porta 8080 e publica o WAR em
`/if-aluno-jsf`. Acesse `http://localhost:8080/if-aluno-jsf/`.
Para outra porta: `mvn clean package cargo:run -Dcargo.servlet.port=9090`.

## Deploy

Por padrão o plugin copia o WAR para `C:\apache-tomcat-9.0.62\webapps` (mesmo
comportamento do `demo-jsf`). Para outro diretório:

```bash
mvn install -Dtomcat.webapps=/opt/tomcat/webapps
```

Acesse: `http://localhost:8080/if-aluno-jsf/`

## Configuração da API

A URL base está em `ifaluno.utils.JsonUtils` (`BASE_API`). Ajuste caso a API
rode em outro host/porta.