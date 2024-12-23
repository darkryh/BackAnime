plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
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