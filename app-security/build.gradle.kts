plugins {
    `java-library`
    id("org.springframework.boot") version "3.5.9" apply false
    id("io.spring.dependency-management") version "1.1.7"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.9")
    }
}

dependencies {
    api(project(":common"))
    api(project(":app-util"))
    implementation(project(":app-persistence"))
    implementation(project(":app-mappers"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.security:spring-security-oauth2-authorization-server:1.5.3")
    implementation("com.nimbusds:nimbus-jose-jwt:9.47")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
