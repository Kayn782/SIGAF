package com.sigaf.app.presentation.screens.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sigaf.app.SigafApplication
import com.sigaf.app.domain.usecase.CompleteWelcomeUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de bienvenida (MVVM).
 *
 * Estados con [StateFlow], efectos de un solo disparo (navegación) con
 * [SharedFlow] para que no se repitan al recomponer.
 */
class WelcomeViewModel(
    private val completeWelcome: CompleteWelcomeUseCase
) : ViewModel() {

    /** Eventos de un solo disparo que la pantalla traduce en navegación. */
    sealed interface Event {
        /** La bienvenida ya quedó guardada: corresponde pasar a permisos. */
        data object ProceedToPermissions : Event
    }

    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val events: SharedFlow<Event> = _events.asSharedFlow()

    /** Pulsar "COMENZAR >": persiste la bienvenida y emite el evento. */
    fun onStartClicked() {
        viewModelScope.launch {
            completeWelcome()
            _events.emit(Event.ProceedToPermissions)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SigafApplication
                WelcomeViewModel(app.container.completeWelcome)
            }
        }
    }
}
