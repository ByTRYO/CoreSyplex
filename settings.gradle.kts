pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://tp.syplex.eu/repository/maven-public/") {
            authentication {
                credentials(PasswordCredentials::class) {
                    val nexusUsername: String by settings
                    val nexusPassword: String by settings

                    username = nexusUsername
                    password = nexusPassword
                }
            }
        }
    }
}

rootProject.name = "Core"

include("common")
include("item")
include("scoreboard")
