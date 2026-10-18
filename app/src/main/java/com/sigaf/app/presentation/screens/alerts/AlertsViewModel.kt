package com.sigaf.app.presentation.screens.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sigaf.app.SigafApplication
import com.sigaf.app.domain.model.Alert
import com.sigaf.app.domain.usecase.security.MarkAlertReadUseCase
import com.sigaf.app.domain.usecase.security.ObserveAlertsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AlertsUiState(
    val alerts: List<Alert> = emptyList(),
    val unreadCount: Int = 0
)

class AlertsViewModel(
    observeAlerts: ObserveAlertsUseCase,
    private val markAlertRead: MarkAlertReadUseCase
) : ViewModel() {

    val uiState: StateFlow<AlertsUiState> = observeAlerts()
        .map { list ->
            AlertsUiState(
                alerts = list.sortedWith(
                    compareByDescending<Alert> { !it.isRead }
                        .thenByDescending { it.timestamp }
                ),
                unreadCount = list.count { !it.isRead }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AlertsUiState()
        )

    fun onAlertClick(alert: Alert) {
        if (alert.isRead) return
        viewModelScope.launch { markAlertRead(alert.id) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SigafApplication
                AlertsViewModel(
                    observeAlerts = app.container.observeAlerts,
                    markAlertRead = app.container.markAlertRead
                )
            }
        }
    }
}
