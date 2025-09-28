plugins {
    java
}

group = "net.nextbattle"
version = "2.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }
    maven { url = uri("https://jitpack.io") }
    maven {
        url = uri("https://repo.menthamc.org/repository/maven-public/")
    }
}

dependencies {
    compileOnly("me.earthme.luminol:luminol-api:1.21.8-R0.1-SNAPSHOT") {
        exclude(group = "com.google.guava", module = "guava")
        exclude(group = "com.google.code.gson", module = "gson")
        exclude(group = "it.unimi.dsi", module = "fastutil")
    }
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.12") {
        exclude(group = "org.apache.logging.log4j", module = "log4j-bom")
    }
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.10") {
        exclude(group = "org.apache.logging.log4j", module = "log4j-bom")
    }
}

// Resources are read from src; avoid duplicates

sourceSets {
    main {
        java.setSrcDirs(listOf("src"))
        resources.setSrcDirs(listOf("src"))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
