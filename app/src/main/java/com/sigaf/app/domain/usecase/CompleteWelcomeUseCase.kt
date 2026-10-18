package com.sigaf.app.domain.usecase

import com.sigaf.app.domain.repository.OnboardingRepository

/**
 * Caso de uso: registrar que el usuario ya vio la pantalla de bienvenida.
 * Se ejecuta al pulsar "COMENZAR >".
 */
class CompleteWelcomeUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke() = repository.markWelcomeSeen()
}
