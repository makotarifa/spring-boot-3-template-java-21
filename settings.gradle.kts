rootProject.name = "spring-boot-3-template-java-21"

include("app-runner", "app-api", "app-service", "app-persistence", "common")

project(":app-runner").projectDir = file("./app-runner")
project(":app-api").projectDir = file("./app-api")
project(":app-service").projectDir = file("./app-service")
project(":app-persistence").projectDir = file("./app-persistence")
project(":common").projectDir = file("./common")
