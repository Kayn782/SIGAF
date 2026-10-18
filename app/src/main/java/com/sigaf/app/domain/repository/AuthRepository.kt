package com.sigaf.app.domain.repository

import com.sigaf.app.domain.model.AuthResult
import com.sigaf.app.domain.model.LoginCredentials
import com.sigaf.app.domain.model.RegisterData
import com.sigaf.app.domain.model.VerificationCode

/**
 * Contrato del repositorio de autenticación (capa domain).
 *
 * La implementación concreta vive en data y se enlaza en di.AppContainer.
 */
interface AuthRepository {

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas.
     */
    suspend fun login(credentials: LoginCredentials): AuthResult

    /**
     * Registra un nuevo usuario.
     */
    suspend fun register(data: RegisterData): AuthResult

    /**
     * Verifica el código enviado al correo electrónico.
     */
    suspend fun verifyCode(code: VerificationCode): AuthResult

    /**
     * Reenvía el código de verificación.
     */
    suspend fun resendCode(): AuthResult

    /**
     * Indica si el usuario tiene sesión activa (simplificado para MVP).
     */
    suspend fun isLoggedIn(): Boolean

    /**
     * Cierra la sesión actual.
     */
    suspend fun logout()
}
