// Archivo raíz de Gradle para SIGAF.
//
// NOTA (AGP 9): el plugin "com.android.application" ya incluye el soporte de
// Kotlin integrado, por eso NO se aplica "org.jetbrains.kotlin.android".
// Solo se añaden los plugin-compañero de Compose y de serialización.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}
