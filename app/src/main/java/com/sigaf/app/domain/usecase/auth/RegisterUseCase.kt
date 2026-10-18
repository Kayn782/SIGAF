package com.sigaf.app.domain.usecase.auth

import com.sigaf.app.domain.model.AuthResult
import com.sigaf.app.domain.model.RegisterData
import com.sigaf.app.domain.repository.AuthRepository

/**
 * Caso de uso: registrar nuevo usuario.
 */
class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(data: RegisterData): AuthResult =
        repository.register(data)
}
