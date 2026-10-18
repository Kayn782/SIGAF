package com.sigaf.app.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sigaf.app.SigafApplication
import com.sigaf.app.domain.model.NotificationPermissionState
import com.sigaf.app.domain.model.OnboardingState
import com.sigaf.app.domain.usecase.ObserveOnboardingStateUseCase
import com.sigaf.app.presentation.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Estado de UI del Splash (modelo de vista inmutable).
 */
data class SplashUiState(
    /** true mientras se consulta DataStore. */
    val loading: Boolean = true,
    /** Ruta a la que debe llevar el botón "INGRESAR AL SISTEMA >". */
    val nextRoute: String = Routes.WELCOME
)

/**
 * ViewModel del Splash (MVVM).
 *
 * Lee el onboarding persistido (DataStore) y decide por dónde entra el
 * usuario:
 *  - Sin bienvenida vista      → WELCOME
 *  - Bienvenida vista, permisos pendientes → PERMISSIONS
 *  - Todo completado           → LOGIN (nunca se salta la autenticación)
 *
 * Regla de negocio: el menú principal (MAIN) SOLO es accesible tras un
 * inicio de sesión o registro exitoso. El Splash nunca navega directo al
 * menú, aunque el onboarding ya esté completo.
 *
 * El estado se expone como [StateFlow] y la UI lo observa con
 * collectAsStateWithLifecycle().
 */
class SplashViewModel(
    private val observeOnboardingState: ObserveOnboardingStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // firstOrNull() es defensivo: si el flujo vacío no emitiera,
            // se asume un onboarding sin completar.
            val state: OnboardingState =
                observeOnboardingState().firstOrNull() ?: OnboardingState()

            _uiState.value = SplashUiState(
                loading = false,
                nextRoute = when {
                    state.notificationPermission != NotificationPermissionState.NOT_ASKED ->
                        Routes.LOGIN
                    state.welcomeSeen -> Routes.PERMISSIONS
                    else -> Routes.WELCOME
                }
            )
        }
    }

    companion object {
        /** Fábrica (inyección manual): el contenedor de la App entrega el caso de uso. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SigafApplication
                SplashViewModel(app.container.observeOnboardingState)
            }
        }
    }
}
