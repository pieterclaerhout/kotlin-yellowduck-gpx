# GPX

[![Release](https://jitpack.io/v/pieterclaerhout/kotlin-yellowduck-gpx.svg)](https://jitpack.io/#pieterclaerhout/kotlin-yellowduck-gpx)
[![Documentation](https://img.shields.io/badge/docs-jitpack-blue)](https://jitpack.io/com/github/pieterclaerhout/kotlin-yellowduck-gpx/latest/javadoc/)
[![License](https://img.shields.io/github/license/pieterclaerhout/kotlin-yellowduck-gpx)](https://raw.githubusercontent.com/pieterclaerhout/kotlin-yellowduck-gpx/main/LICENSE)

A Kotlin library which can parse and generate GPX files.

## How to add

First, add the JitPack repository:

```kotlin
repositories { 
    mavenCentral()
    maven {
        url = uri("https://jitpack.io") 
    }
}
```

Then add the library as a dependency:

```kotlin
dependencies {
	implementation("com.github.pieterclaerhout:kotlin-yellowduck-gpx:1.0.2")
}
```

JitPack URL:

https://jitpack.io/#pieterclaerhout/kotlin-yellowduck-gpx

## Testing the documentation

```
./gradlew clean -Pgroup=com.github.pieterclaerhout -Pversion=v1.0.4 -xtest build publishToMavenLocal
```