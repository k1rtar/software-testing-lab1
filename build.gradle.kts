plugins {
    kotlin("jvm") version "2.0.0"
    id("jacoco")
}

group = "com.kirtar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Явно добавляем зависимости для JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.3")
}

tasks.test {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                // Требуем 100% покрытия
                minimum = "1.0".toBigDecimal()
            }
        }
    }
}

tasks.build {
    finalizedBy(tasks.jacocoTestReport, tasks.jacocoTestCoverageVerification)
}

kotlin {
    jvmToolchain(21)
}
