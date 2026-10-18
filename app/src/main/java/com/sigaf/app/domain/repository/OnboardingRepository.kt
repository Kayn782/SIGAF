package com.sigaf.app.domain.repository

import com.sigaf.app.domain.model.OnboardingState
import kotlinx.coroutines.flow.Flow

/**
 * Contrato (capa domain) del repositorio de onboarding.
 *
 * La UI y los casos de uso dependen SOLO de esta interfaz; la implementación
 * concreta (DataStore) vive en la capa data y se enlaza en [com.sigaf.app.di.AppContainer].
 */
interface OnboardingRepository {

    /** Expone el estado del onboarding en tiempo real (DataStore → Flow). */
    fun observeOnboarding(): Flow<OnboardingState>

    /** Marca la pantalla de bienvenida como vista. */
    suspend fun markWelcomeSeen()

    /** Persiste la decisión del usuario sobre el permiso de notificaciones. */
    suspend fun setNotificationPermission(granted: Boolean)
}
