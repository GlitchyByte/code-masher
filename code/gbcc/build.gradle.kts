plugins {
    application
    id("org.graalvm.buildtools.native") version "0.9.5"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

dependencies {
    // Main dependencies.
    // -- None.
    // Test dependencies.
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
}

tasks.test {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
    maxParallelForks = 8
}

// Setup build info.
group = "com.glitchybyte"
version = "1.2.0"
val mainPackage = "$group.gbcc"

application {
    // Set app entry point.
    mainClass.set("$mainPackage.App")
}
