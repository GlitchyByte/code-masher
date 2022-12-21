// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: MIT-0

plugins {
    id("com.glitchybyte.java-published-library-conventions")
}

publishing {
    repositories {
        maven {
            name = "gCloud"
            url = uri("artifactregistry://us-west1-maven.pkg.dev/glitchybyte-cloud/maven")
        }
    }
}
