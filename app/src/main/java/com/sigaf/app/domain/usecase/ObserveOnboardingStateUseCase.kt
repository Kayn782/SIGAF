package com.sigaf.app.domain.usecase

import com.sigaf.app.domain.model.OnboardingState
import com.sigaf.app.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso: observar el estado del onboarding.
 *
 * El Splash lo usa para decidir por dónde entra el usuario:
 * bienvenida → permisos → menú principal.
 */
class ObserveOnboardingStateUseCase(
    private val repository: OnboardingRepository
) {
    operator fun invoke(): Flow<OnboardingState> = repository.observeOnboarding()
}
