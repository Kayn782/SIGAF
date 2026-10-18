package com.sigaf.app.domain.repository

import com.sigaf.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de autenticación.
 * La implementación concreta vive en la capa `data` (FakeAuthRepository en el MVP).
 */
interface AuthRepository {

    /** Sesión actual del usuario (null si no ha iniciado sesión). */
    val currentUser: Flow<UserProfile?>

    /** Correo recordado por el usuario (para prellenar el login). */
    val rememberedEmail: Flow<String>

    suspend fun signIn(email: String, password: String, rememberMe: Boolean): Result<UserProfile>

    suspend fun register(name: String, email: String, password: String): Result<Unit>

    /** Verifica el código de 4 dígitos enviado al correo. */
    suspend fun verifyCode(code: String): Result<Unit>

    suspend fun signOut()
}
