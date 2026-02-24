import com.codeskraps.weather.ConfigData

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.codeskraps.weather"
    compileSdk = ConfigData.compileSdk

    defaultConfig {
        applicationId = "com.arklan.weather"
        minSdk = ConfigData.minSdk
        targetSdk = ConfigData.targetSdk
        versionCode = 21
        versionName = "3.0.1"
        base.archivesName.set("weekly-weather-v$versionName.$versionCode")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = ConfigData.isMinifyRelease
            //applicationIdSuffix = ".release"
            //versionNameSuffix = "-RELEASE"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isShrinkResources = true
            //signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            isMinifyEnabled = ConfigData.isMinifyDebug
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_19)
    }
}

dependencies {
    implementation(project(mapOf("path" to ":feature:common")))
    implementation(project(mapOf("path" to ":feature:weather")))
    implementation(project(mapOf("path" to ":feature:maps")))
    implementation(project(mapOf("path" to ":feature:settings")))
    implementation(project(mapOf("path" to ":core:umami")))
    implementation(project(mapOf("path" to ":core:local")))
    implementation(project(mapOf("path" to ":core:location")))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.android.compose.material3)
    implementation(libs.android.compose.material.icons)
    implementation(libs.androidx.core.splashscreen)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.graphics)
    implementation(libs.androidx.compose.tooling.preview)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    implementation(libs.coroutines.test)
    testImplementation(libs.junit.junit)
    androidTestImplementation(composeBom)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.compose.test.junit4)
    debugImplementation(libs.androidx.compose.tooling)
    debugImplementation(libs.androidx.compose.test.manifest)
}