import eu.syplex.Publish

plugins {
    java
    `maven-publish`
    `java-library`

    kotlin("jvm") version "1.8.20"

    id("eu.syplex.publish") version "1.0.2"
}

group = "eu.syplex.core"
version = "1.2.8"

dependencies {
    api(project(":common"))
    api(project(":item"))
    api(project(":scoreboard"))
}

kotlin.jvmToolchain(17)

subprojects {
    apply {
        plugin<JavaPlugin>()
        plugin<JavaLibraryPlugin>()
        plugin<MavenPublishPlugin>()
        plugin<Publish>()
    }
}

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://tp.syplex.eu/repository/maven-public/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        implementation("org.jetbrains:annotations:24.0.0")
        implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")
    }

    java {
        withSourcesJar()
        withJavadocJar()
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }

    tasks.javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    publish {
        useSyplexNexusRepos()
        publishComponent("java")
    }

    if (project.name.contains("examples")) return@allprojects
    publishing {
        publications.create<MavenPublication>("maven") {
            publish.configurePublication(this)
            pom {
                url.set("https://gitlab.syplex.eu/Riccardo/Core")
                developers {
                    developer {
                        name.set("Riccardo")
                        url.set("https://gitlab.syplex.eu/Riccardo")
                    }
                    developer {
                        name.set("Merry")
                        url.set("https://gitlab.syplex.eu/Merry")
                    }
                }
            }
        }
        repositories.maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = findProperty("nexusUsername") as String
                    password = findProperty("nexusPassword") as String
                }
                setUrl(publish.getRepository())
                name = "SyplexNexus"
            }
        }
    }
}