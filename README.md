# spring-boot-3-template-java-21

- `app-runner`: the main Spring Boot application (the service implementation)
How to build and run locally:

Configuration: The project uses application.yml files (in each module's resources) instead of application.properties.

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

 Example endpoints (AppTest):

 - List: `GET /api/app-tests` -> returns a JSON array of `AppTestDto` items.
 - Create: `POST /api/app-tests` with JSON body `{ "name": "something" }` -> creates a new item.

Example cURL:

```bash
curl -s http://localhost:8080/api/app-tests | jq
curl -s -H "Content-Type: application/json" -d '{"name":"hello"}' -X POST http://localhost:8080/api/app-tests
```

Notes:
- `common` is a library module - you can publish it separately for reuse.
- If you split microservices later, keep `common` as a separate git submodule or artifact for reuse across services.
