group = "be.yellowduck"
version = "1.0.10"

val myArtifactId: String = rootProject.name
val myArtifactGroup: String = project.group.toString()
val myArtifactVersion: String = project.version.toString()

val myGithubUsername = "pieterclaerhout"
val myGithubDescription = "A GPX library written in Kotlin"
val myGithubHttpUrl = "https://github.com/${myGithubUsername}/kotlin-yellowduck-gpx"
val myGithubIssueTrackerUrl = "https://github.com/${myGithubUsername}/kotlin-yellowduck-gpx/issues"
val myLicense = "MIT"
val myLicenseUrl = "https://raw.githubusercontent.com/pieterclaerhout/kotlin-yellowduck-gpx/main/LICENSE"

val myDeveloperName = "Pieter Claerhout"

plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "1.9.25"
    id("org.jetbrains.dokka") version "1.9.20"
    `maven-publish`
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.19.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    implementation("org.glassfish.jaxb:txw2:2.2.11")

    testImplementation("org.assertj:assertj-core:3.18.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
    from("LICENCE") {
        into("META-INF")
    }
}

tasks {
    dokkaHtml {
        dokkaSourceSets.configureEach {
            includeNonPublic.set(false)
            skipEmptyPackages.set(true)
        }
    }
}

val dokkaJavadocJar by tasks.creating(Jar::class) {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaJavadoc.get().outputDirectory.get())
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        register("gprRelease", MavenPublication::class) {
            groupId = myArtifactGroup
            artifactId = myArtifactId
            version = myArtifactVersion

            from(components["java"])

            artifact(sourcesJar)
            artifact(dokkaJavadocJar)

            pom {
                packaging = "jar"
                name.set(myArtifactId)
                description.set(myGithubDescription)
                url.set(myGithubHttpUrl)
                scm {
                    url.set(myGithubHttpUrl)
                }
                issueManagement {
                    url.set(myGithubIssueTrackerUrl)
                }
                licenses {
                    license {
                        name.set(myLicense)
                        url.set(myLicenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(myGithubUsername)
                        name.set(myDeveloperName)
                    }
                }
            }

        }
    }
}