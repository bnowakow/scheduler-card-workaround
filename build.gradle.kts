// https://kotlinlang.org/docs/get-started-with-jvm-gradle-project.html#explore-the-build-script

plugins {
    kotlin("jvm") version "1.9.21"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "bnowakowski.pl.home_assistant"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.7")
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.16.1")
    implementation("org.seleniumhq.selenium:selenium-firefox-driver:4.16.1")
    implementation("org.seleniumhq.selenium:selenium-safari-driver:4.16.1")
    implementation("org.seleniumhq.selenium:selenium-java:4.16.1")
    implementation("org.seleniumhq.selenium:selenium-support:4.16.1")
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

