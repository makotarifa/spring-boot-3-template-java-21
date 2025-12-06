rootProject.name = "spring-boot-3-template-java-21"

include("app", "app-api", "app-service", "app-persistence", "common")

project(":app").projectDir = file("./app")
project(":app-api").projectDir = file("./app-api")
project(":app-service").projectDir = file("./app-service")
project(":app-persistence").projectDir = file("./app-persistence")
project(":common").projectDir = file("./common")
