plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("jacoco")
}

android {
    namespace = "com.fishing.conditions"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.fishing.conditions"
        minSdk = 23
        targetSdk = 36
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

// Enable Kapt support for Kotlin 2.0+
kapt {
    correctErrorTypes = true
    arguments {
        arg("kapt.kotlin.2.0.language.support", "true")
    }
}

// JaCoCo configuration for test coverage
configure<JacocoPluginExtension> {
    toolVersion = "0.8.11"
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")

    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.12.3")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

    // Room
    val roomVersion = "2.8.4"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Maps - OpenStreetMap (free, no API key required)
    implementation("org.osmdroid:osmdroid-android:6.1.20")
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    // Location Services
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Dependency Injection (Hilt)
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    // Testing
    testImplementation("junit:junit:4.13.2")

    // JUnit 5 (Jupiter) - Modern unit testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:6.0.2")

    // MockK - Kotlin mocking library
    testImplementation("io.mockk:mockk:1.14.9")
    testImplementation("io.mockk:mockk-android:1.14.9")

    // Coroutines testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    // Truth - Assertion library
    testImplementation("com.google.truth:truth:1.4.5")

    // Turbine - Flow testing
    testImplementation("app.cash.turbine:turbine:1.2.1")

    // Robolectric - Android unit tests on JVM
    testImplementation("org.robolectric:robolectric:4.16.1")

    // Hilt testing
    testImplementation("com.google.dagger:hilt-android-testing:2.52")
    kaptTest("com.google.dagger:hilt-android-compiler:2.52")

    // Android Testing
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Hilt Android testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.52")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.52")
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
