# Template Multi-Module Spring Boot Project

This project is prepared for future migration into microservices. It is already divided into modules:

- `app`: the main Spring Boot application (the service implementation)
- `common`: shared DTOs, exceptions, and utilities used across services

How to build and run locally:

1. Build the project:

```bash
./gradlew clean build
```

2. Run Postgres and pgAdmin (optional):

```bash
cd docker/postgres
docker-compose up -d
```

3. Run the application from the root (starts `app`):

```bash
cd ..
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/template \
SPRING_DATASOURCE_USERNAME=template \
SPRING_DATASOURCE_PASSWORD=template \
./gradlew :app:bootRun
```

4. Swagger UI (OpenAPI): http://localhost:8080/swagger-ui/index.html

Notes:
- `common` is a library module - you can publish it separately for reuse.
- If you split microservices later, keep `common` as a separate git submodule or artifact for reuse across services.
