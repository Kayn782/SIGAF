package com.sigaf.app.presentation.navigation

/**
 * Rutas de navegación de SIGAF (solo strings, centralizados aquí).
 * Se usan con Navigation Compose: una ruta = una pantalla del grafo.
 *
 * Orden del flujo inicial:
 * SPLASH → WELCOME → PERMISSIONS → LOGIN → (REGISTER / VERIFY) → HOME
 */
object Routes {

    // --- Flujo de bienvenida (implementado) ---
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val PERMISSIONS = "permissions"

    // --- Autenticación (pantallas provisionales) ---
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val VERIFY = "verify"

    // --- Menú principal con bottom navigation ---
    const val MAIN = "main"
    const val HOME = "home"
    const val ANALYZER = "analyzer"
    const val ALERTS = "alerts"
    const val HISTORY = "history"
    const val PROFILE = "profile"
}
