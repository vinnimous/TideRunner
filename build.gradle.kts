plugins {
    kotlin("android") version "1.5.31"
    id("com.android.application") version "7.0.2"
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.fishing.conditions"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
