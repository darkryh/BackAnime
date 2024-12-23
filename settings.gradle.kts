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
    }
}

rootProject.name = "BackAnime"
include(":app")
include(":Anime-Core")
include(":Source-JkAnime")
include(":Source-LatAnime")

project(":Source-JkAnime").projectDir = File(rootDir, "src/es/Source-JkAnime")
project(":Source-LatAnime").projectDir = File(rootDir, "src/es/Source-LatAnime")