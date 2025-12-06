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
