package com.sigaf.app.domain.usecase.auth

import com.sigaf.app.domain.model.AuthResult
import com.sigaf.app.domain.model.LoginCredentials
import com.sigaf.app.domain.repository.AuthRepository

/**
 * Caso de uso: iniciar sesión.
 */
class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(credentials: LoginCredentials): AuthResult =
        repository.login(credentials)
}
