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
    implementation("info.picocli:picocli:4.6.3")
    implementation(project(":glib"))
    implementation("org.mdkt.compiler:InMemoryJavaCompiler:1.3.0")
    implementation(project(":gspring"))
}

// Setup build info.
group = "com.glitchybyte.codemasher"
version = "3.0.3"

application {
    // Set app entry point.
    mainClass.set("com.glitchybyte.codemasher.App")
}
