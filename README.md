# spring-boot-3-template-java-21

- `app-runner`: the main Spring Boot application (the service implementation)
How to build and run locally:

Configuration: The project uses application.yml files (in each module's resources) instead of application.properties.

Security and DX highlights:
- API versioning under `/api/v1`
- Stateless auth with JWT and HttpOnly cookie `AUTH_TOKEN`
- CORS configuration with credential safety
- Fixed-window rate limiting for `/api/v1/login` and `/api/v1/register`
- Standardized error handling via Spring `ProblemDetail`
- Convenience endpoints: `GET /api/v1/me`, `POST /api/v1/logout`
- Strong password validation on register (uppercase, lowercase, number, special)

1. Build the project:

```bash
./gradlew clean build
```

2. Run Postgres and pgAdmin (optional):

```bash
cd docker/postgres
docker-compose up -d
```

3. Run the application from the root (starts `app-runner`):

```bash
cd ..
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/template \
SPRING_DATASOURCE_USERNAME=template \
SPRING_DATASOURCE_PASSWORD=template \
./gradlew :app-runner:bootRun

Or use the helper task `runSingle` (defaults to `app-runner`):

```bash
./gradlew runSingle
# or explicitly: ./gradlew runSingle -PrunProject=app-runner
```
```

4. Swagger UI (OpenAPI): http://localhost:8080/swagger-ui/index.html

Run with Docker Compose (app-runner + db + pgAdmin):

```bash
cd docker/postgres
docker-compose up --build -d
```

This builds the `app-runner` image using the multi-stage Dockerfile and starts the app.

You can override the internal server port and host mapping used by Docker Compose with `SERVER_PORT` and `HOST_PORT` environment variables:

```bash
# Run the app inside the container on port 8081 and map to host port 8081:
HOST_PORT=8081 SERVER_PORT=8081 docker-compose up --build -d
```

Bootstrap (Windows):

```powershell
./scripts/bootstrap.ps1
# Loads env from .env (quoted values supported) and starts docker/postgres
```

Example endpoints (AppTest):

- List: `GET /api/app-tests` -> returns a JSON array of `AppTestDto` items.
- Create: `POST /api/app-tests` with JSON body `{ "name": "something" }` -> creates a new item.

Example cURL:

```bash
curl -s http://localhost:8080/api/app-tests | jq
curl -s -H "Content-Type: application/json" -d '{"name":"hello"}' -X POST http://localhost:8080/api/app-tests
```

Auth endpoints:
- Register: `POST /api/v1/register` body `{ "username": "user", "password": "password123" }` → `204 No Content`
- Login: `POST /api/v1/login` body `{ "username": "user", "password": "password123" }` → sets `Set-Cookie: AUTH_TOKEN=...; HttpOnly;` and returns `{ "username": "...", "expiresAt": "..." }`
- Health: `GET /api/v1/health` → `200 OK`

Dev seed user:
- Username: `user`
- Password: `password123`

Configuration (dev/prod):
- `app.security.enabled`: enable security stack
- `app.security.auth.algorithm`: `hs256` (dev) or `rs256` (prod)
- `app.security.auth.issuer`: token issuer
- `app.security.auth.jwt.secret`: HS256 secret (dev)
- `app.security.auth.rsa.private-key-pem` / `public-key-pem`: RS256 keys (prod)
- `app.security.cors.allowed-origins`: comma-separated origins; credentials only enabled when this is set
- `app.security.cookie.secure` and `app.security.cookie.same-site`
- `app.rate-limit.paths`, `app.rate-limit.capacity`, `app.rate-limit.refill-period-seconds`, `app.rate-limit.trust-x-forwarded-for`

Notes:
- `common` is a library module - you can publish it separately for reuse.
- If you split microservices later, keep `common` as a separate git submodule or artifact for reuse across services.
