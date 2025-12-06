plugins {
    `java-library`
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
