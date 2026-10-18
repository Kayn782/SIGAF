package com.sigaf.app.data.repository

import com.sigaf.app.domain.model.AuthResult
import com.sigaf.app.domain.model.LoginCredentials
import com.sigaf.app.domain.model.RegisterData
import com.sigaf.app.domain.model.VerificationCode
import com.sigaf.app.domain.repository.AuthRepository
import kotlinx.coroutines.delay

/**
 * Implementación provisional (fake) del repositorio de autenticación.
 *
 * MVP front-end: simula validaciones básicas y respuestas exitosas/errores
 * para que las pantallas funcionen sin backend. Se reemplazará por
 * implementación remota en Bloque 2 (Retrofit).
 */
class AuthRepositoryImpl : AuthRepository {

    private var loggedIn = false
    private var pendingEmail: String? = null

    override suspend fun login(credentials: LoginCredentials): AuthResult {
        // Simulación de latencia de red
        delay(600)
        return if (credentials.email.isNotBlank() && credentials.password.length >= 6) {
            loggedIn = true
            AuthResult.Success
        } else {
            AuthResult.Error("Correo o contraseña inválidos")
        }
    }

    override suspend fun register(data: RegisterData): AuthResult {
        delay(700)
        return if (data.fullName.isNotBlank() && data.email.contains("@") &&
            data.password.length >= 8 && data.acceptTerms
        ) {
            pendingEmail = data.email
            AuthResult.Success
        } else {
            AuthResult.Error("Verifica los datos e inténtalo de nuevo")
        }
    }

    override suspend fun verifyCode(code: VerificationCode): AuthResult {
        delay(500)
        // Código simulado para desarrollo: acepta 4 dígitos no vacíos
        return if (code.code.length == 4 && code.code.all { it.isDigit() }) {
            pendingEmail = null
            loggedIn = true
            AuthResult.Success
        } else {
            AuthResult.Error("Código de verificación inválido")
        }
    }

    override suspend fun resendCode(): AuthResult {
        delay(400)
        return if (pendingEmail != null) {
            AuthResult.Success
        } else {
            AuthResult.Error("No hay un correo pendiente de verificación")
        }
    }

    override suspend fun isLoggedIn(): Boolean = loggedIn

    override suspend fun logout() {
        loggedIn = false
        pendingEmail = null
    }
}
