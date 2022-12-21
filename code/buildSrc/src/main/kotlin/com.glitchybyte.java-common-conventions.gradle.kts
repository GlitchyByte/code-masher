// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: MIT-0

plugins {
    id("java")
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}

testing {
    suites.withType(JvmTestSuite::class) {
        useJUnitJupiter("5.8.2")
    }
}

tasks.withType<Test>().configureEach {
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}
