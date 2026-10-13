# API reference

[← Back to README](../README.md)

## Features

- **Students**: create, list, filter, edit and delete.
  - Fields: name, email, enrollment number, CPF, height, phone, date of birth and active.
  - Validation: name with 3 to 100 characters, valid email, CPF in the `XXX.XXX.XXX-XX` format, height greater than zero, phone like `(16) 99123-4567` and date of birth in the past. All fields are required.
  - Email and enrollment number are unique. A duplicate value returns `409 Conflict`.
- **Schools**: create, list, filter, edit and delete.
  - Fields: name (3 to 100 characters) and level (`FUNDAMENTAL`, `MEDIO` or `SUPERIOR`).
- **Filters**: text fields are matched by substring, case-insensitive. An omitted filter is ignored.
- **Errors**: validation failures and malformed data return `400` with a standard JSON body (`mensagem`, `mensagens` and `campos`, which holds the error for each field).

## API Endpoints

All endpoints live under the `/demo-api` prefix.

| Method | Route                                | Description                                    |
| ------ | ------------------------------------ | ---------------------------------------------- |
| GET    | `/alunos`                            | Lists all students                             |
| GET    | `/alunos/{id}`                       | Gets a student by id (`404` if not found)      |
| GET    | `/aluno?id=&nome=&email=&matricula=` | Filters students (all parameters are optional) |
| POST   | `/alunos`                            | Creates a student (`201`)                      |
| PUT    | `/alunos/{id}`                       | Updates a student (`404` if not found)         |
| DELETE | `/alunos/{id}`                       | Deletes a student                              |
| GET    | `/escolas`                           | Lists all schools                              |
| GET    | `/escolas/{id}`                      | Gets a school by id (`404` if not found)       |
| GET    | `/escola?id=&nome=&nivel=`           | Filters schools (all parameters are optional)  |
| POST   | `/escolas`                           | Creates a school (`201`)                       |
| PUT    | `/escolas/{id}`                      | Updates a school (`404` if not found)          |
| DELETE | `/escolas/{id}`                      | Deletes a school                               |

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
