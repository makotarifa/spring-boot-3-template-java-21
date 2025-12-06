Docker Compose for PostgreSQL
=============================

Start PostgreSQL and pgAdmin for local development using Docker Compose.

Commands:

```bash
# Start services in background
docker-compose up -d

# Stop and remove containers
docker-compose down
```

Connection details (defaults):

- Host: localhost
- Port: 5432 (container maps to host)
- Database: template
- Username: template
- Password: template

If you want to run your Spring Boot app in the same compose network (and resolve the DB host as 'db'), set `SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/template` in your app environment.

PgAdmin is available at http://localhost:8081 with the credentials configured in compose.

¿Qué es pgAdmin?
-----------------
pgAdmin es una herramienta gráfica (GUI) de administración y desarrollo para bases de datos PostgreSQL. Con pgAdmin puedes:

- Conectarte y gestionar servidores PostgreSQL.
- Navegar y editar tablas, esquemas y datos.
- Ejecutar consultas SQL manuales y ver resultados.
- Gestionar usuarios, privilegios y backups.

Es muy útil para desarrollo local y para tareas de administración sin usar la línea de comandos.
