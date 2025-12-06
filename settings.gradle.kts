rootProject.name = "spring-boot-3-template-java-21"

include("app", "common")

project(":app").projectDir = file("./app")
project(":common").projectDir = file("./common")
