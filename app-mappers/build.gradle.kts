plugins {
    `java-library`
}

group = "com.angelmorando"
version = "0.0.1-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    api(project(":app-domain"))
    api(project(":app-dtos"))
    api(project(":app-persistence"))
    implementation(project(":app-util"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.mapstruct:mapstruct:1.6.3")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

java { toolchain.languageVersion.set(JavaLanguageVersion.of(21)) }
