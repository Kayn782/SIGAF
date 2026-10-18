plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sigaf.app"

    // AGP 9: la versión de compilación se declara con el nuevo DSL.
    // API 37 = plataforma instalada en este equipo.
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.sigaf.app"
        // minSdk 26 (Android 8.0): canales de notificación, Keystore moderno,
        // WorkManager estable y NotificationListenerService sin problemas.
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // AGP 9: reemplaza a isMinifyEnabled / isShrinkResources.
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // ------------------------------------------------------------------
    // Jetpack Compose (la BOM fija versiones coherentes)
    // ------------------------------------------------------------------
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    debugImplementation(libs.compose.ui.tooling)

    // ------------------------------------------------------------------
    // AndroidX / Ciclo de vida / Navegación
    // ------------------------------------------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.lifecycle.runtime.compose)   // collectAsStateWithLifecycle()
    implementation(libs.lifecycle.viewmodel.compose) // viewModel() + viewModelFactory {}
    implementation(libs.navigation.compose)

    // ------------------------------------------------------------------
    // Red: Retrofit + OkHttp + kotlinx.serialization (se usan desde el Bloque 2)
    // ------------------------------------------------------------------
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    // El interceptor de logging quedará solo en la variante debug en el Bloque 2.
    implementation(libs.okhttp.logging)

    // ------------------------------------------------------------------
    // Almacenamiento local: Room (historial, Bloque 4) + DataStore (Bloque 6)
    // ------------------------------------------------------------------
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.datastore.preferences)

    // ------------------------------------------------------------------
    // Seguridad local: Keystore + EncryptedSharedPreferences (Bloque 2)
    // ------------------------------------------------------------------
    implementation(libs.security.crypto)

    // ------------------------------------------------------------------
    // Segundo plano: WorkManager (Bloque 5)
    // ------------------------------------------------------------------
    implementation(libs.work.runtime.ktx)

    // ------------------------------------------------------------------
    // Pruebas
    // ------------------------------------------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}
