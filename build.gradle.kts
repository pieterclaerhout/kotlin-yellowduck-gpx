import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    `maven-publish`
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation("org.glassfish.jaxb:txw2:2.2.11")
    testImplementation("org.assertj:assertj-core:3.18.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

group = "be.yellowduck"
version = "1.0.2"

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

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
    from("LICENCE.md") {
        into("META-INF")
    }
}

publishing {
    publications {
        register("gprRelease", MavenPublication::class) {
            groupId = myArtifactGroup
            artifactId = myArtifactId
            version = myArtifactVersion

            from(components["java"])

            artifact(sourcesJar)

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