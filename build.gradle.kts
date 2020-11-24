tasks.named<Wrapper>("wrapper") {
    // Automatically set distribution type all when updating gradlew.
    distributionType = Wrapper.DistributionType.ALL
}

plugins {
    application
}

repositories {
    mavenCentral()
}

java {
    // Java version for the app.
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}

tasks.named("test", Test::class) {
    // Use JUnit 5 (Jupiter) with 8 parallel test execution and full logging.
    useJUnitPlatform()
    maxParallelForks = 8
    testLogging.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
}

dependencies {
    // Main dependencies.
//    implementation("com.glitchybyte:gcc:1.0.0")
    // Test dependencies.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

// Setup build info.
group = "com.glitchybyte"
version = "1.0.0"
val mainPackage = "$group.gcc"

application {
    // Set app entry point.
    mainClass.set("$mainPackage.App")
}
