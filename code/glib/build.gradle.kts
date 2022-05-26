// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    maxParallelForks = 4
}

dependencies {
    // Main dependencies.
    api("com.google.code.gson:gson:2.9.0")
    // Test dependencies.
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

// Setup build info.
group = "com.glitchybyte.glib"
version = "1.1.4"

tasks.named<Javadoc>("javadoc") {
    title = "${rootProject.name} v${version} API"
}
