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

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.8.2")
        }
    }
}

tasks.withType<Test>().configureEach {
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}

dependencies {
    api("com.google.code.gson:gson:2.9.0")
}

// Setup build info.
group = "com.glitchybyte.glib"
version = "1.2.3"

tasks.named<Javadoc>("javadoc") {
    title = "${rootProject.name} v${version} API"
}
