import org.jetbrains.kotlin.analyzer.ModuleInfo

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.8"
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.beryx.jlink") version "3.0.1"
}

group = "com.facilitapix.printers.escpos.server"
version = "0.0.1"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.facilitapix.printers.escpos.server.ApplicationKt")
    mainModule.set("com.facilitapix.printers.escpos.server")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.github.anastaciocintra:escpos-coffee:4.1.0")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation(kotlin("stdlib-jdk8"))
}

kotlin {
    jvmToolchain(17)
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "com.facilitapix.ApplicationKt"
        }
    }
    jlinkZip {
        group = "distribution"
    }
}

jlink {
    imageZip = project.file("${buildDir}/distributions/app-server.zip")
    addOptions("--strip-debug", "--compress=2", "--no-header-files", "--no-man-pages")
    launcher {
        name = "app"
    }
    addExtraDependencies(
        "jackson",
        "kotlinx",
    )
    mergedModule {
        excludeProvides(
            mapOf("servicePattern" to "reactor.blockhound.*"),
        )
    }
}