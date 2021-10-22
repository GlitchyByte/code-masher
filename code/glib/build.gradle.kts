// Copyright 2020-2021 GlitchyByte
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

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 4
}

dependencies {
    // Main dependencies.
    api("com.google.code.gson:gson:2.8.8")
    // Test dependencies.
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
}

// Setup build info.
group = "com.glitchybyte.glib"
version = "1.0.0"
