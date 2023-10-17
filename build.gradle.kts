 // Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
    }
}
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
    id("com.android.library") version "8.1.2" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}