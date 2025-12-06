rootProject.name = "template"

include("app", "common")

project(":app").projectDir = file("./app")
project(":common").projectDir = file("./common")
