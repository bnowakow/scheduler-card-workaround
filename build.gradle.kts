val ktor_version: String = "2.3.13"

// https://kotlinlang.org/docs/get-started-with-jvm-gradle-project.html#explore-the-build-script

plugins {
    kotlin("jvm") version "2.0.0-RC1"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20"
}

group = "bnowakowski.pl.home_assistant"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.26.0")
    implementation("org.seleniumhq.selenium:selenium-firefox-driver:4.26.0")
    implementation("org.seleniumhq.selenium:selenium-safari-driver:4.26.0")
    implementation("org.seleniumhq.selenium:selenium-java:4.26.0")
    implementation("org.seleniumhq.selenium:selenium-support:4.26.0")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("io.ktor:ktor-client-auth:$ktor_version")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
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

