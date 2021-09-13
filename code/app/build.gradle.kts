plugins {
    application
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

// Create uber jar at the end of build, after tests.
tasks.named("build") {
    finalizedBy("uberJar")
}

// Register uber jar task.
tasks.register("uberJar", Jar::class) {
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    group = "Build"
    description = "Assembles a jar archive containing everything."
    archiveClassifier.set("uber")
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })
}
