package com.sigaf.app.domain.model

/**
 * Credenciales mínimas para iniciar sesión.
 */
data class LoginCredentials(
    val email: String,
    val password: String,
    val rememberMe: Boolean = false
)

/**
 * Datos necesarios para crear una cuenta.
 */
data class RegisterData(
    val fullName: String,
    val email: String,
    val password: String,
    val acceptTerms: Boolean
)

/**
 * Código de verificación de correo electrónico.
 */
data class VerificationCode(
    val code: String
)

/**
 * Resultado de autenticación simplificado para el MVP.
 */
sealed interface AuthResult {
    data object Success : AuthResult
    data class Error(val message: String) : AuthResult
}
