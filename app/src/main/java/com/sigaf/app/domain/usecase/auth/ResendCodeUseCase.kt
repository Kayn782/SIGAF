package com.sigaf.app.domain.usecase.auth

import com.sigaf.app.domain.model.AuthResult
import com.sigaf.app.domain.repository.AuthRepository

/**
 * Caso de uso: reenviar código de verificación.
 */
class ResendCodeUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): AuthResult =
        repository.resendCode()
}
