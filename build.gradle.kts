import org.jetbrains.kotlin.cli.common.isWindows

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.8"
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "3.0.1"
}

group = "com.facilitapix.printers.escpos.manager"
version = "1.9.0"

repositories {
    mavenCentral()
}

application {
    mainModule.set("com.facilitapix.printers.escpos.manager.servergui")
    mainClass.set("com.facilitapix.printers.escpos.manager.servergui.Application")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.swing", "javafx.fxml")
}

dependencies {
    implementation("org.reflections:reflections:0.10.2")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.8.0-RC2")

//    // Ignite + Dagger
//    implementation("com.gluonhq:ignite-dagger:1.2.2")
//    implementation("com.google.dagger:dagger:2.50")
//    annotationProcessor("com.google.dagger:dagger-compiler:2.50")

    implementation("com.dustinredmond.fxtrayicon:FXTrayIcon:4.0.1")
    implementation("com.github.anastaciocintra:escpos-coffee:4.1.0")
    implementation("io.github.palexdev:materialfx:11.17.0")

    // Ktor
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Testing
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "com.facilitapix.printers.escpos.manager.servergui.Application"
        }
    }
    jlinkZip {
        group = "distribution"
    }
}

tasks.named<JavaExec>("run") {
    // Usa uma propriedade de projeto para definir o modo de desenvolvimento
    args = listOf("--devMode=${project.findProperty("devMode") ?: "false"}")
}

jlink {
    imageZip = project.file("${layout.buildDirectory}/distributions/app-server.zip")
    addOptions("--strip-debug", "--compress=2", "--no-header-files", "--no-man-pages")
    launcher {
        name = "FacilitaPrinter"
    }
    addExtraDependencies("javafx", "jackson", "kotlinx")
    forceMerge("kotlinx-coroutines-core", "kotlinx-coroutines-javafx")
    mergedModule {
        excludeProvides(
            mapOf("servicePattern" to "reactor.blockhound.*"),
        )
    }
    jpackage {
        icon = "src/main/resources/facilita-pix-logo.ico"
        if (isWindows) {
            installerType = "msi"
            installerOptions = listOf("--win-dir-chooser", "--win-menu", "--win-shortcut")
        }
    }
}