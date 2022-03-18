import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "dev.peterevans"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jooq:jooq:3.16.5")

    testImplementation(kotlin("test"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    testImplementation("org.testcontainers:testcontainers:1.16.3")
    testImplementation("org.testcontainers:junit-jupiter:1.16.3")
    testImplementation("org.testcontainers:postgresql:1.16.3")

    testImplementation("org.postgresql:postgresql:42.3.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
