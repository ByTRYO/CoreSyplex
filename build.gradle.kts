plugins {
    java
    `java-library`
}

group = "eu.syplex"
version = "1.0.0-SNAPSHOT"

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