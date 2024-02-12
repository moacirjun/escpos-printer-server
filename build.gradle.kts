plugins {
    id("org.javamodularity.moduleplugin") version "1.8.12"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.javamodularity.moduleplugin")

    repositories {
        mavenCentral()
    }
}
