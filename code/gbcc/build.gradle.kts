// Copyright 2020-2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

plugins {
    application
    id("org.graalvm.buildtools.native") version "0.9.5"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    // Main dependencies.
    implementation(project(":glib"))
    implementation("org.mdkt.compiler:InMemoryJavaCompiler:1.3.0")
    // Test dependencies.
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 4
}

// Setup build info.
group = "com.glitchybyte.gbcc"
version = "2.0.0"

application {
    // Set app entry point.
    mainClass.set("com.glitchybyte.gbcc.App")
}
