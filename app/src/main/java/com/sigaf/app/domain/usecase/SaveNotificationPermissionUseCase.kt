package com.sigaf.app.domain.usecase

import com.sigaf.app.domain.repository.OnboardingRepository

/**
 * Caso de uso: persistir la decisión del usuario sobre el permiso de
 * lectura de notificaciones ("Otorgar" / "Denegar").
 */
class SaveNotificationPermissionUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(granted: Boolean) =
        repository.setNotificationPermission(granted)
}
