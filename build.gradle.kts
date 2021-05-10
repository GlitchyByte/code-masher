plugins {
    application
}

repositories {
    mavenCentral()
}

java {
    // Java version for the app.
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks.named("test", Test::class) {
    // Use JUnit 5 (Jupiter) with 8 parallel test execution and full logging.
    useJUnitPlatform()
    maxParallelForks = 8
    testLogging.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
}

dependencies {
    // Main dependencies.
    // -- None.
    // Test dependencies.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

// Setup build info.
group = "com.glitchybyte"
version = "1.1.0"
val mainPackage = "$group.gcc"

application {
    // Set app entry point.
    mainClass.set("$mainPackage.App")
}

// Create uber jar at the end of build, after tests.
tasks.named("build") {
    finalizedBy("uberJar")
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Register uber jar task.
tasks.register("uberJar", Jar::class) {
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
