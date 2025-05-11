val ktor_version: String = "2.3.13"
val selenium_version: String = "4.32.0"

// https://kotlinlang.org/docs/get-started-with-jvm-gradle-project.html#explore-the-build-script

plugins {
    kotlin("jvm") version "2.1.20"
    application
    id("com.gradleup.shadow") version "8.3.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20"
}

group = "bnowakowski.pl.home_assistant"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:$selenium_version")
    implementation("org.seleniumhq.selenium:selenium-firefox-driver:$selenium_version")
    implementation("org.seleniumhq.selenium:selenium-safari-driver:$selenium_version")
    implementation("org.seleniumhq.selenium:selenium-java:$selenium_version")
    implementation("org.seleniumhq.selenium:selenium-support:$selenium_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-auth:$ktor_version")
testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
        mainClass.set("pl.bnowakowski.home_assistant_workaround.MainKt")
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "pl.bnowakowski.home_assistant_workaround.Main"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

