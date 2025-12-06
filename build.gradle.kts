plugins {
	id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {
	group = "com.angelmorando"
	version = "0.0.1-SNAPSHOT"
	repositories { mavenCentral() }
}

subprojects {
	apply(plugin = "io.spring.dependency-management")
	// Configure the dependency management extension to import Spring Boot BOM
	configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
		imports {
			mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.8")
		}
	}
	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

// Helper task to run a single module using bootRun. By default it runs app-runner.
// Usage: ./gradlew runSingle -PrunProject=app-runner
val runProjectName: String? = project.findProperty("runProject")?.toString()
tasks.register("runSingle") {
	val target = runProjectName ?: "app-runner"
	dependsOn(":$target:bootRun")
}
