import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
    kotlin("android") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    kotlin("kapt")                            // org.jetbrains.kotlin.kapt
    alias(libs.plugins.dagger.hilt)          // com.google.dagger.hilt.android
    alias(libs.plugins.kotlin.compose)       // org.jetbrains.kotlin.plugin.compose
}

android {
    namespace = "com.example.ingsw_24_25_dietiestates25"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ingsw_24_25_dietiestates25"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
dependencies {


    //webview
    implementation ("androidx.compose.ui:ui:1.5.4")
    implementation ("androidx.compose.ui:ui-tooling:1.5.4")
    implementation ("androidx.compose.material:material:1.5.4")
    implementation ("androidx.activity:activity-compose:1.7.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    //github oauth
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")

    // Facebook SDK (Versione aggiornata)
    implementation ("com.facebook.android:facebook-android-sdk:[8,9)")
    implementation ("com.auth0.android:jwtdecode:2.0.0")
    implementation ("com.facebook.android:facebook-core:latest.release")
    implementation ("com.facebook.android:facebook-login:latest.release")
    implementation ("com.facebook.android:facebook-share:latest.release")
    implementation ("com.facebook.android:facebook-applinks:latest.release")

    // Jetpack Compose
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.material:material:1.5.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.3")
    implementation("androidx.compose.compiler:compiler:1.5.10")
    implementation ("androidx.compose.material3:material3:1.2.0") // O versione più recente
    implementation ("androidx.compose.material:material:1.4.3")


    // Google OAuth 2.0
    implementation("androidx.credentials:credentials:1.5.0-alpha05")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0-alpha05")

    // Ktor Client per rete e serializzazione
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("io.ktor:ktor-client-logging:2.3.4")

    // Kotlin Coroutines e Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // Librerie AndroidX
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation(libs.androidx.material3.android)
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Google Play Services
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation(libs.googleid)
    implementation("androidx.browser:browser:1.8.0")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.3")

    // Hilt per Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

}