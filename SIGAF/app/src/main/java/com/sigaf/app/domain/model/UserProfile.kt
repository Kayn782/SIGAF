package com.sigaf.app.domain.model

/** Perfil del usuario autenticado. */
data class UserProfile(
    val name: String,
    val email: String,
    /** Nivel de protección global de 0 a 100 (ej. 87 %). */
    val protection: Int
) {
    /** Nivel textual del indicador de protección. */
    val protectionLabel: String
        get() = when {
            protection >= 80 -> "ALTO"
            protection >= 60 -> "MEDIO"
            else -> "BAJO"
        }

    /** Iniciales para el avatar (máx. 2 letras). */
    val initials: String
        get() = name.trim()
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifBlank { "US" }
}
