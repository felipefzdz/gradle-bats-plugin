plugins {
    `java-gradle-plugin`
    `groovy`
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.12.0"
}

group = "com.felipefzdz.gradle.bats"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(localGroovy())
    implementation("commons-io:commons-io:2.8.0")
    testImplementation(gradleTestKit())
    testImplementation(platform("org.spockframework:spock-bom:2.0-M4-groovy-3.0"))
    testImplementation("org.spockframework:spock-core")
    testImplementation("org.spockframework:spock-junit4")
    testImplementation("junit:junit:4.13.1")
}

gradlePlugin {
    plugins {
        val bats by plugins.creating {
            id = "com.felipefzdz.gradle.bats"
            implementationClass = "com.felipefzdz.gradle.bats.BatsPlugin"
            displayName = "Bats"
            description = "The Bats Gradle plugin executes Bats tests."
        }
    }
}

pluginBundle {
    website = "https://github.com/felipefzdz/gradle-bats-plugin"
    vcsUrl = "https://github.com/felipefzdz/gradle-bats-plugin.git"
    tags = listOf("bats", "shell", "bash", "testing")
}

val functionalTestSourceSet = sourceSets.create("functionalTest") { }

configurations[functionalTestSourceSet.implementationConfigurationName].extendsFrom(configurations["testImplementation"])
configurations[functionalTestSourceSet.runtimeOnlyConfigurationName].extendsFrom(configurations["runtimeOnly"])

val functionalTest = tasks.register("functionalTest", Test::class) {
    description = "Runs functional tests."
    group = "verification"

    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath

    // should find functional test output summary and use that as the timestamp comparison
    // if there should be any updates that would affect the integration test
    outputs.upToDateWhen { false }
    shouldRunAfter(tasks.named("test"))
    useJUnitPlatform()
    testLogging {
        showStandardStreams = false // true
        // events "passed", "skipped", "failed"
        showExceptions = true
        showCauses = true
        minGranularity = 2
        minGranularity = 4
        displayGranularity = 0
    }
}

tasks.named("check").configure {
    dependsOn(functionalTest)
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

tasks.named("publishPlugins").configure {
    dependsOn(functionalTest)
}
