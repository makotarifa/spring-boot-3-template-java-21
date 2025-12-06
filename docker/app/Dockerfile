FROM gradle:8.6-jdk21 AS builder
WORKDIR /home/gradle/project
COPY . /home/gradle/project
RUN gradle :app-runner:bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=app-runner/build/libs/app-runner-0.0.1-SNAPSHOT.jar
COPY --from=builder /home/gradle/project/${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
