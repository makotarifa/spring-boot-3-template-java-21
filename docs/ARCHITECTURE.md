# Preparando la arquitectura para microservicios

Esta guía describe cómo estructurar el proyecto para facilitar una futura separación en microservicios y cómo preparar OAuth y manejo centralizado de errores.

1. Estructura de paquetes y módulos
   - `common` (módulo compartido): excepciones y utilidades que pueden ser compartidas entre servicios.
   - `app-domain` (módulo de dominio): entidades, objetos de valor (VO) y repositorios (interfaces) del dominio.
   - `app-dtos` (módulo de DTO): DTOs compartidos por la API, usados por `app-api`.
   - `app-mappers` (módulo de mapeo): mapeadores entre `domain` <-> `dto` (MapStruct o instancias manuales).
   - `app-runner` (módulo principal): módulo ejecutable que arranca Spring Boot y compone `app-api`, `app-service` y `app-persistence`.
   - `app-api`: controladores y DTOs expuestos al exterior (REST API). Depende de `app-service`, `app-dtos` y `app-mappers`.
    - `app-service`: lógica de negocio y servicios.
    - `app-persistence`: persistencia, mappers, DAOs y repositorios (encargados de consultar base de datos).

Ejemplo de estructura del proyecto (multi-módulo):

- common/
   - src/main/java/com/angelmorando/template/exception
   - src/main/java/com/angelmorando/template/dto
- app-runner/
   - src/main/java/com/angelmorando/template/controller
   - src/main/java/com/angelmorando/template/service
   - src/main/resources

Al adoptar un diseño multi-módulo, puedes extraer servicios en el futuro en módulos independientes.

2. Estructura de paquetes
   - `controller`: Controladores REST (expuestos al exterior).
   - `service`: Lógica de negocio.
   - `repository`: Acceso a datos (MyBatis mappers/repositorios).
   - `dto`: Objetos de transferencia y respuestas; preferentemente en su propio módulo `app-dtos`.
   - `config`: Configuraciones, beans comunes, CORS, etc.
   - `security`: Configuración y filtros de seguridad.
   - `exception`: Manejadores y DTOs de error centralizados.

   DAO vs Repository:
   - DAO: encapsula las consultas SQL y la interacción con la BD (por ejemplo con MyBatis mappers, JdbcTemplate u otra librería). Es el responsable de la conversión a objetos y del manejo de transacciones en el nivel más bajo.
   - Repository: expone la API de dominio (operaciones como `save`, `find`, `findAll`) y utiliza la DAO internamente. Esto ayuda a mantener la lógica de negocio desacoplada de la tecnología de persistencia.

   Implementación actual en el proyecto:
   - DTO `AppTestDto` se encuentra en `app-dtos` (antes en `common`).
<<<<<<< HEAD
   - DAO MyBatis `AppTestDao` y su XML controlan las consultas SQL.
=======
   - Mapper MyBatis `AppTestMapper` y su XML controlan las consultas SQL.
   - `AppTestDao` usa el mapper para ejecutar consultas.
>>>>>>> origin/main
   - `AppTestRepository` (interface) se encuentra en `app-domain` y `AppTestRepositoryImpl` en `app-persistence` como implementación.

   Mantener una separación clara facilita extraer módulos/microservicios: cada microservicio tendría su propio `controller` y `service` pero podrían compartir `dto`, `security` y `exception` en una librería común.

2. Autenticación y autorización (OAuth2/JWT)
   - Use `spring-boot-starter-oauth2-resource-server` para exponer el servicio como recurso protegido mediante JWTs.
   - Configure `spring.security.oauth2.resourceserver.jwt.jwk-set-uri` para apuntar al servidor de autorización (JWK uri).
   - Para una API pública que también acepte inicios de sesión de usuario, agregue `spring-boot-starter-oauth2-client`.

3. Manejo de errores
   - Centralice con `@ControllerAdvice` y `ApiError` para respuestas consistentes.
   - Use códigos HTTP correctos y mensajes apropiados para clientes front-end.

4. Infraestructura
   - Docker / docker-compose para desarrollo local (ya incluido).
   - Para producción: K8s + Helm o una plataforma de PaaS.
   - CI/CD: build/publish per microservice + integration tests.

   7. OpenAPI / Swagger
      - Add `springdoc-openapi-starter-webmvc-ui` to `app` module.
      - Default docs are available at `/v3/api-docs` and Swagger UI at `/swagger-ui/index.html`.
      - Keep DTOs and API contract stable in `common` so microservices can share contract libraries if needed.

5. Separación en microservicios
   - Empiece separando por contexto de negocio (dominios): e.g., `users`, `orders`, `payments`.
   - Mantenga contratos (DTOs) estables y documentados (OpenAPI).
   - Considere un API Gateway + AuthN/AuthZ centralizado.

6. Otras recomendaciones
   - Añadir `spring-boot-starter-actuator` y `micrometer` para monitorización.
   - Añadir `OpenAPI/Swagger` para documentar las APIs.
   - Mantener configuraciones externas: `SPRING_APPLICATION_JSON`, `SPRING_DATASOURCE_*` o `Spring Cloud Config`.
