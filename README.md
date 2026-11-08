# University Management System

A Java 21, Spring Boot, PostgreSQL REST API. Start the application and database together with Docker Compose:

```powershell
$env:POSTGRES_PASSWORD = "database-password"
$env:INITIAL_ADMIN_PASSWORD = "initial-admin-password"
docker compose up --build
```

Both passwords must be at least 12 characters. The initial admin username defaults to `admin` and can be changed with `INITIAL_ADMIN_USERNAME`. Do not use development passwords in production.

For a locally running application, open `http://localhost:8080/`. The root URL returns a public API status response; it is not a web UI. The application does not configure HTTPS, so use `http://`, not `https://`. API data endpoints still require authentication.

## Authentication

Authentication uses server-side sessions, BCrypt password hashes, and a CSRF token for state-changing requests:

1. `GET /api/auth/csrf` and retain the returned `token`.
2. `POST /api/auth/login` with JSON `{"username":"admin","password":"..."}` and the token in the returned `headerName` header. Keep the session cookie.
3. For subsequent POST, PUT, and DELETE requests, send both the session cookie and the CSRF header.
4. `POST /api/auth/logout` with the session cookie and CSRF header.

The first admin account is created from `INITIAL_ADMIN_USERNAME` and `INITIAL_ADMIN_PASSWORD` when the application starts. An admin can provision role accounts with `POST /api/users`:

```json
{"username":"teacher1","password":"a-strong-password-12","role":"TEACHER"}
```

Allowed roles are `ADMIN`, `TEACHER`, and `STUDENT`; passwords must contain at least 12 characters. Account creation is admin-only and responses never include password hashes.

## Access policy

| Resource | Admin | Teacher | Student |
| --- | --- | --- | --- |
| Admin records | Full CRUD | No access | No access |
| Teacher records | Full CRUD | Read | Read |
| Student records | Full CRUD | Read, create, update | No access |
| User accounts | Create accounts | No access | No access |

## REST endpoints

All record endpoints use JSON. Create requests return `201 Created` with a `Location` header; successful updates return `200 OK`; deletes return `204 No Content`; missing records return `404 Not Found`.

| Method | Endpoint | Purpose |
| --- | --- | --- |
| GET | `/api/admins`, `/api/admins/{id}` | List or fetch admins |
| POST | `/api/admins` | Create an admin record |
| PUT | `/api/admins/{id}` | Replace an admin record |
| DELETE | `/api/admins/{id}` | Delete an admin record |
| GET | `/api/teachers`, `/api/teachers/{id}` | List or fetch teachers |
| POST | `/api/teachers` | Create a teacher |
| PUT | `/api/teachers/{id}` | Replace a teacher |
| DELETE | `/api/teachers/{id}` | Delete a teacher |
| GET | `/api/students`, `/api/students/{id}` | List or fetch students |
| POST | `/api/students` | Create a student |
| PUT | `/api/students/{id}` | Replace a student |
| DELETE | `/api/students/{id}` | Delete a student |

Student JSON fields are `name` and `dept`; teacher fields are `name`, `pos`, and `dept`; admin fields are `name`. Generated identifiers are returned in response bodies.

## Local testing

Run the test suite with `./mvnw test` (PowerShell: `.\mvnw.cmd test`). Tests use an in-memory H2 database and do not require Docker.
