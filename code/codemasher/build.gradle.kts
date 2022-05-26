// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

plugins {
    application
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
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 4
}

// Setup build info.
group = "com.glitchybyte.codemasher"
version = "3.0.0"

application {
    // Set app entry point.
    mainClass.set("com.glitchybyte.codemasher.App")
}
