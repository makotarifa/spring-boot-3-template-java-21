plugins {
    java
}

group = "com.angelmorando"
version = "0.0.1-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    implementation(project(":app-persistence"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
