package com.sigaf.app.presentation.screens.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sigaf.app.SigafApplication
import com.sigaf.app.domain.usecase.SaveNotificationPermissionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de permisos de notificaciones (MVVM).
 *
 * Persiste la decisión ("Otorgar" / "Denegar") en DataStore y avisa a la
 * pantalla mediante un evento [SharedFlow] de un solo disparo para que
 * navegue exactamente una vez.
 *
 * Nota de arquitectura: aquí solo se guarda la decisión del usuario en el
 * front-end. La concesión REAL del acceso a notificaciones (Settings de
 * NotificationListenerService) llega con el servicio de segundo plano.
 */
class PermissionsViewModel(
    private val saveNotificationPermission: SaveNotificationPermissionUseCase
) : ViewModel() {

    /** Eventos de un solo disparo que la pantalla traduce en navegación. */
    sealed interface Event {
        /** La decisión quedó persistida: continuar al siguiente destino. */
        data object DecisionSaved : Event
    }

    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val events: SharedFlow<Event> = _events.asSharedFlow()

    /** Evita navegaciones duplicadas por doble toque rápido. */
    private var saving = false

    fun onGrant() = decide(granted = true)

    fun onDeny() = decide(granted = false)

    private fun decide(granted: Boolean) {
        if (saving) return
        saving = true
        viewModelScope.launch {
            saveNotificationPermission(granted)
            _events.emit(Event.DecisionSaved)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SigafApplication
                PermissionsViewModel(app.container.saveNotificationPermission)
            }
        }
    }
}
