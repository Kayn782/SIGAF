package com.sigaf.app.data.repository

import com.sigaf.app.data.preference.OnboardingPreferencesDataSource
import com.sigaf.app.domain.model.OnboardingState
import com.sigaf.app.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación concreta del repositorio de onboarding (capa data).
 *
 * Traduce las preferencias de DataStore al modelo de dominio
 * ([OnboardingState]) para que el domain nunca conozca claves ni enums
 * de persistencia.
 */
class OnboardingRepositoryImpl(
    private val preferences: OnboardingPreferencesDataSource
) : OnboardingRepository {

    override fun observeOnboarding(): Flow<OnboardingState> =
        preferences.onboardingState

    override suspend fun markWelcomeSeen() =
        preferences.setWelcomeSeen(seen = true)

    override suspend fun setNotificationPermission(granted: Boolean) =
        preferences.setNotificationPermission(granted)
}
