import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaStringVersion: String by project
val javaVersion = JavaVersion.toVersion(javaStringVersion)
val javaVirtualMachineTarget = JvmTarget.fromTarget(javaStringVersion)
val backAnimeVersion : String by project

plugins {
    id("java-library")
    id("maven-publish")
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

kotlin {
    compilerOptions {
        jvmTarget = javaVirtualMachineTarget
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            afterEvaluate {
                from(components["java"])
            }

            groupId = "com.ead.lib"
            artifactId = "back-anime-es-jkanime"
            version = backAnimeVersion
        }
    }
}


dependencies {
    api(project(":Anime-Core"))
}