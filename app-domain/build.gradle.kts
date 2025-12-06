plugins {
    `java-library`
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.angelmorando"
version = "0.0.1-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

java { toolchain.languageVersion.set(JavaLanguageVersion.of(21)) }
