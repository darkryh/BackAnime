pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "BackAnime"
include(":app")
include(":Anime-Core")
include(":Source-JkAnime")
include(":Source-LatAnime")

include(":Anime-Es-Bundle")

project(":Source-JkAnime").projectDir = File(rootDir, "src/es/Source-JkAnime")
project(":Source-LatAnime").projectDir = File(rootDir, "src/es/Source-LatAnime")

