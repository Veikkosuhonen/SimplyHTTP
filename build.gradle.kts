import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "me.veikk"
version = "1.1.2"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
    implementation("com.google.code.gson:gson:2.8.9")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}

compose.desktop {

    application {

        mainClass = "MainKt"

        nativeDistributions {

            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            modules("java.instrument", "java.net.http", "java.sql", "jdk.unsupported")

            packageName = "SimplyHTTP"
            packageVersion = "1.1.2"
            version = "1.1.2"
            description = "HTTP API Testing Tool"
            copyright = "GPLv3"
            vendor = "Veikmaster"

            linux {
                iconFile.set(project.file("images/icon.png"))
                appCategory = "utils"
            }

            windows {
                shortcut = true
                iconFile.set(project.file("images/icon.ico"))
            }
        }
    }
}