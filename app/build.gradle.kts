// build.gradle.kts (module: app)
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.gestinpermisosapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gestinpermisosapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2" // Actualizado a 1.5.2
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:1.5.2") // Actualizado a 1.5.2
    implementation("androidx.compose.ui:ui-graphics:1.5.2") // Actualizado a 1.5.2
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.2") // Actualizado a 1.5.2
    implementation("androidx.compose.material3:material3:1.2.0") // Puedes mantener esta versión

    // Accompanist Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.32.0") // Actualizado

    // Google Maps Compose
    implementation("com.google.maps.android:maps-compose:2.11.4")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.6.0")

    // Coil para Jetpack Compose
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Google Play Services
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.2") // Actualizado a 1.5.2
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.2") // Actualizado a 1.5.2
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.2") // Actualizado a 1.5.2
}
