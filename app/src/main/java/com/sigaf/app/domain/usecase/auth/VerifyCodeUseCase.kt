package com.sigaf.app.domain.usecase.auth

import com.sigaf.app.domain.model.AuthResult
import com.sigaf.app.domain.model.VerificationCode
import com.sigaf.app.domain.repository.AuthRepository

/**
 * Caso de uso: verificar código de correo.
 */
class VerifyCodeUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(code: VerificationCode): AuthResult =
        repository.verifyCode(code)
}
