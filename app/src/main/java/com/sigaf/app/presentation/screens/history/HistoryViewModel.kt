package com.sigaf.app.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sigaf.app.SigafApplication
import com.sigaf.app.domain.model.HistoryEntry
import com.sigaf.app.domain.usecase.security.ClearHistoryUseCase
import com.sigaf.app.domain.usecase.security.ObserveHistoryUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HistoryUiState(
    val entries: List<HistoryEntry> = emptyList()
)

class HistoryViewModel(
    observeHistory: ObserveHistoryUseCase,
    private val clearHistory: ClearHistoryUseCase
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = observeHistory()
        .map { list -> HistoryUiState(entries = list) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState()
        )

    fun onClearHistory() {
        viewModelScope.launch { clearHistory() }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SigafApplication
                HistoryViewModel(
                    observeHistory = app.container.observeHistory,
                    clearHistory = app.container.clearHistory
                )
            }
        }
    }
}
