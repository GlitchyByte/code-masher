// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

plugins {
    id("com.glitchybyte.java-application-conventions")
    id("com.google.cloud.artifactregistry.gradle-plugin") version "2.2.0"
}

repositories {
    maven {
        url = uri("artifactregistry://us-west1-maven.pkg.dev/glitchybyte-cloud/public-maven")
    }
}

dependencies {
    implementation("info.picocli:picocli:4.6.3")
    implementation("com.glitchybyte.glib:glib:1.4.0")
    implementation("org.mdkt.compiler:InMemoryJavaCompiler:1.3.0")
    implementation("com.glitchybyte.gspring:gspring:1.1.1")
}

// Setup build info.
group = "com.glitchybyte.codemasher"
version = "3.1.0"

application {
    // Set app entry point.
    mainClass.set("com.glitchybyte.codemasher.App")
}
