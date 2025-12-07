plugins {
    `java-library`
}

group = "com.angelmorando"
version = "0.0.1-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    api(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-actuator")
    api("io.micrometer:micrometer-core")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


java { toolchain.languageVersion.set(JavaLanguageVersion.of(21)) }
