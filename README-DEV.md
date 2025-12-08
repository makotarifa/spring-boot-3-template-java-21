Development environment (Postgres + Authorization Server)

Prerequisites
- Docker (for local Postgres container), or a local Postgres instance.

Quick start with Docker Compose
1. Start local Postgres:

```bash
docker compose -f docker-compose.dev.yml up -d
```

2. Start the application in dev profile (it will use the Postgres DB and enable the Authorization Server):

```bash
./gradlew :app-runner:bootRun -Dspring.profiles.active=dev
```

3. Check that Flyway executed and tables were created in the `template` database:

```bash
docker exec -it template-postgres psql -U template -d template -c "\dt"
```

Notes
- `app.security.enabled` is set to `true` in `application-dev.yml` so the Authorization Server is enabled for dev.
- If `spring-authorization-server` cannot resolve during build, ensure your environment allows access to Maven Central and the resolution of `org.springframework.security:spring-authorization-server` artifact. If your network blocks access to external repositories, consider mirroring the artifact or adding repository credentials.

Next steps
- Once the Authorization Server dependency compiles, the server will expose the usual endpoints at `/oauth2/authorize`, `/oauth2/token`, and the JWK set at `/.well-known/jwks.json`.
- You can seed additional clients using `RegisteredClientDataLoader` in `app-security`.

If you want, I can:
- Try a different version for the Authorization Server dependency and attempt a full build.
- Add a health check endpoint for the Authorization server.
- Add integration tests for issuing tokens using the `demo-client`.
