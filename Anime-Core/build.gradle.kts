import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaStringVersion: String by project
val javaVersion = JavaVersion.toVersion(javaStringVersion)
val javaVirtualMachineTarget = JvmTarget.fromTarget(javaStringVersion)
val backAnimeVersion : String by project

plugins {
    id("java-library")
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

dependencies {
    // coroutines
    api(libs.kotlinx.coroutines.core)

    // json handler
    api(libs.json)

    // jsoup scrapper
    api(libs.jsoup)

    // http client
    api(platform(libs.okhttp.bom))
    api(libs.okhttp)
    api(libs.okhttp.interceptor)
}