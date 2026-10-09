# IF-Aluno

CRUD de **alunos** e **escolas** desenvolvido na disciplina Linguagem de Programação 2 (IFSP).
Uma API REST em Spring Boot guarda os dados no PostgreSQL. Um frontend JSF/PrimeFaces consome essa API.

## Funcionalidades

- **Alunos**: cadastrar, listar, filtrar, editar e excluir.
  - Campos: nome, email, matrícula, CPF, altura, telefone, data de nascimento e ativo.
  - Validações: nome com 3 a 100 caracteres, email válido, CPF no formato `XXX.XXX.XXX-XX`, altura maior que zero, telefone como `(16) 99123-4567` e data de nascimento no passado. Todos os campos são obrigatórios.
  - Email e matrícula são únicos. Um valor duplicado retorna `409 Conflict`.
- **Escolas**: cadastrar, listar, filtrar, editar e excluir.
  - Campos: nome (3 a 100 caracteres) e nível (`FUNDAMENTAL`, `MEDIO` ou `SUPERIOR`).
- **Filtros**: os textos são buscados por trecho, sem diferenciar maiúsculas de minúsculas. Um filtro omitido é ignorado.
- **Erros**: validações e dados malformados retornam `400` com um JSON padronizado (`mensagem`, `mensagens` e `campos`, que traz o erro de cada campo).

## Arquitetura

```
Navegador ──► Frontend JSF (Tomcat 9, :8080) ──HTTP/JSON──► API Spring Boot (:8081) ──JPA──► PostgreSQL (Docker, :5433)
```

| Pasta | Conteúdo |
|---|---|
| `/` (raiz) | API REST, pacote `com.demo`, com módulos `aluno`, `escola` e `config` |
| `frontend/if-aluno-jsf` | Frontend JSF/PrimeFaces, empacotado como WAR |

O frontend acessa a API em `http://localhost:8081/demo-api`. Essa URL fica em `ifaluno.utils.JsonUtils.BASE_API`.

## Requisitos

| Item | Versão |
|---|---|
| JDK | 21 (a API usa Java 21; o frontend compila para Java 17) |
| Docker / Docker Compose | qualquer versão recente (para subir o PostgreSQL 13.22) |
| Maven | não precisa instalar: use o wrapper `mvnw` (Maven 3.9.16) |

Principais dependências: Spring Boot 4.1.0, springdoc-openapi 2.8.6, JSF (Mojarra) 2.4.0, PrimeFaces 14.0.0, Weld 3.1.9 e Tomcat 9.0.98, que o Cargo baixa sozinho.

## Como rodar

Rode os passos na ordem, cada um em um terminal.

**Linux / macOS**

```bash
# 1. Banco (na raiz)
docker compose up -d

# 2. API (na raiz)
./mvnw spring-boot:run

# 3. Frontend
cd frontend/if-aluno-jsf
./mvnw clean package cargo:run
```

**Windows (cmd / PowerShell)**

```bat
:: 1. Banco (na raiz)
docker compose up -d

:: 2. API (na raiz)
mvnw.cmd spring-boot:run

:: 3. Frontend
cd frontend\if-aluno-jsf
mvnw.cmd clean package cargo:run
```

No IntelliJ, você também pode subir a API com o botão Run em `DemoApplication`.

As tabelas são criadas automaticamente na primeira execução (`ddl-auto=update`).

## Portas e URLs

| Serviço | Porta | URL |
|---|---|---|
| PostgreSQL (Docker) | 5433 | `jdbc:postgresql://localhost:5433/Aluno` (usuário `aluno_user`, senha `aluno_password`) |
| API (Spring Boot) | 8081 | http://localhost:8081/demo-api |
| Swagger UI | 8081 | http://localhost:8081/swagger-ui/index.html |
| Frontend (Tomcat 9) | 8080 | http://localhost:8080/if-aluno-jsf/ |

## Endpoints da API

Todos os endpoints ficam sob o prefixo `/demo-api`.

| Método | Rota | Descrição |
|---|---|---|
| GET | `/alunos` | Lista todos os alunos |
| GET | `/alunos/{id}` | Busca um aluno por id (`404` se não existir) |
| GET | `/aluno?id=&nome=&email=&matricula=` | Filtra alunos (todos os parâmetros são opcionais) |
| POST | `/alunos` | Cria um aluno (`201`) |
| PUT | `/alunos/{id}` | Atualiza um aluno (`404` se não existir) |
| DELETE | `/alunos/{id}` | Exclui um aluno |
| GET | `/escolas` | Lista todas as escolas |
| GET | `/escolas/{id}` | Busca uma escola por id (`404` se não existir) |
| GET | `/escola?id=&nome=&nivel=` | Filtra escolas (todos os parâmetros são opcionais) |
| POST | `/escolas` | Cria uma escola (`201`) |
| PUT | `/escolas/{id}` | Atualiza uma escola (`404` se não existir) |
| DELETE | `/escolas/{id}` | Exclui uma escola |

Exemplo de corpo para `POST /demo-api/alunos`:

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

As coleções do Postman ficam em `src/main/resources/`.

## Build e testes

```bash
./mvnw verify                                  # API (o teste precisa do Postgres rodando)
cd frontend/if-aluno-jsf && ./mvnw package     # gera target/if-aluno-jsf.war
```

O GitHub Actions (`.github/workflows/ci.yml`) roda os dois builds a cada push na `main` e em cada pull request. No CI, o PostgreSQL sobe como container de serviço.
