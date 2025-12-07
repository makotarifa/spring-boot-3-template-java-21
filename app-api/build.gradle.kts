plugins {
    java
}

group = "com.angelmorando"
version = "0.0.1-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    implementation(project(":app-service"))
    implementation(project(":common"))
    implementation(project(":app-dtos"))
    implementation(project(":app-mappers"))
    implementation(project(":app-util"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
