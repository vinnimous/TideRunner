plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("jacoco")
}

android {
    namespace = "com.fishing.conditions"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fishing.conditions"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isDebuggable = true
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true  // Remove unused resources
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false

            // R8 optimizations
            isCrunchPngs = true  // Optimize PNG files
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true  // Explicitly enable BuildConfig (replaces deprecated gradle.properties setting)
        viewBinding = false  // Disable if not using view binding
        dataBinding = false  // Disable if not using data binding
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        // Disable checks that are not relevant or too strict
        disable += setOf("MissingTranslation", "ExtraTranslation")
        // Treat warnings as errors in CI/CD
        abortOnError = false
        checkReleaseBuilds = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        animationsDisabled = true
    }
}



// JaCoCo configuration for test coverage
configure<JacocoPluginExtension> {
    toolVersion = "0.8.11"
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2024.04.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Maps - OpenStreetMap (free, no API key required)
    implementation("org.osmdroid:osmdroid-android:6.1.20")
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")

    // Location Services
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Dependency Injection (Hilt)
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Testing
    testImplementation("junit:junit:4.13.2")

    // JUnit 5 (Jupiter) - Modern unit testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")

    // MockK - Kotlin mocking library
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("io.mockk:mockk-android:1.13.13")

    // Coroutines testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Truth - Assertion library
    testImplementation("com.google.truth:truth:1.1.5")

    // Turbine - Flow testing
    testImplementation("app.cash.turbine:turbine:1.0.0")

    // Robolectric - Android unit tests on JVM
    testImplementation("org.robolectric:robolectric:4.11.1")

    // Hilt testing
    testImplementation("com.google.dagger:hilt-android-testing:2.52")
    kspTest("com.google.dagger:hilt-android-compiler:2.52")

    // Android Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Hilt Android testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.52")
    kspAndroidTest("com.google.dagger:hilt-android-compiler:2.52")
}

tasks.withType<Test> {
    useJUnitPlatform() // Enable JUnit 5
}

// Configure JaCoCo after project evaluation when Android tasks are created
afterEvaluate {
    tasks.register<JacocoReport>("jacocoTestReport") {
        dependsOn("testDebugUnitTest")

        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(false)
        }

        val fileFilter = listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "android/**/*.*",
            "**/databinding/**",
            "**/generated/**"
        )

        val debugTree = fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
            exclude(fileFilter)
        }

        val mainSrc = "${project.projectDir}/src/main/java"

        sourceDirectories.setFrom(files(mainSrc))
        classDirectories.setFrom(files(debugTree))
        executionData.setFrom(fileTree(layout.buildDirectory.get()) {
            include("jacoco/testDebugUnitTest.exec")
        })
    }

    // Ensure test runs before coverage report
    tasks.named("testDebugUnitTest") {
        finalizedBy("jacocoTestReport")
    }
}
