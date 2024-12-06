plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Dipendenze di Ktor
    implementation(libs.ktor.client.core) // Aggiungi la versione corretta
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.serialization)
    implementation(libs.kotlinx.coroutines.core) // Coroutines
    implementation(libs.ktor.client.core.v234)
    implementation(libs.ktor.client.cio.v234) // Motore CIO per Ktor Client
    implementation(libs.ktor.client.content.negotiation) // Per gestire JSON
    implementation(libs.ktor.serialization.kotlinx.json) // Serializzazione JSON
    implementation(libs.kotlinx.coroutines.core.v173) // Coroutines
    implementation(libs.ktor.client.logging)

    // Dipendenze del progetto
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.volley)
    implementation(libs.transport.runtime)

    // Dipendenze per i test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //NUOVE DIPENDENZE
    //1.
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.hilt.navigation.fragment)
    //2.
    implementation(libs.androidx.navigation.compose)

}