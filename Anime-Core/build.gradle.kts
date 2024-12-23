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
            artifactId = "back-anime-core"
            version = backAnimeVersion
        }
    }
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.json)
    api(libs.jsoup)
    api(platform(libs.okhttp.bom))
    api(libs.okhttp)
    api(libs.okhttp.interceptor)
}