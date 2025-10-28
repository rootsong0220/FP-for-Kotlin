plugins {
    kotlin("jvm") version "2.2.20"
    id("io.kotest") version "6.0.4"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotestVersion = "6.0.4"
dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:${kotestVersion}")
    testImplementation("io.kotest:kotest-assertions-core:${kotestVersion}")
}

tasks.test {
    useJUnitPlatform()
    include("**/*")
}
kotlin {
    jvmToolchain(21)
}