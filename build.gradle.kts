plugins {
    java
    `java-library`
    id("io.freefair.lombok") version "8.0.1"
}

group = "eu.syplex"
version = "1.0.0"

subprojects {
    apply {
        plugin<JavaPlugin>()
        plugin<JavaLibraryPlugin>()
    }
}

dependencies {
    api(project(":common"))
    api(project(":item"))
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        implementation("org.jetbrains:annotations:24.0.0")
    }

    java {
        withSourcesJar()
        withJavadocJar()
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
}