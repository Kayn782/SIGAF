package com.sigaf.app.presentation.screens.analyzer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sigaf.app.SigafApplication
import com.sigaf.app.domain.model.AnalysisInput
import com.sigaf.app.domain.model.AnalysisResult
import com.sigaf.app.domain.model.AnalysisTab
import com.sigaf.app.domain.model.RiskLevel
import com.sigaf.app.domain.usecase.analysis.AnalyzeContentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AnalyzerUiState(
    val selectedTab: AnalysisTab = AnalysisTab.MESSAGE,
    val inputText: String = "",
    val isAnalyzing: Boolean = false,
    val result: AnalysisResult? = null,
    val errorMessage: String? = null
)

class AnalyzerViewModel(
    private val analyzeContentUseCase: AnalyzeContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyzerUiState())
    val uiState: StateFlow<AnalyzerUiState> = _uiState.asStateFlow()

    private var isSubmitting = false

    fun onTabSelected(tab: AnalysisTab) {
        _uiState.update { it.copy(selectedTab = tab, result = null, errorMessage = null) }
    }

    fun onInputChange(value: String) {
        _uiState.update { it.copy(inputText = value, errorMessage = null) }
    }

    fun onAnalyzeClick() {
        if (isSubmitting) return
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingresa contenido a analizar") }
            return
        }
        isSubmitting = true
        viewModelScope.launch {
            _uiState.update { it.copy(isAnalyzing = true, errorMessage = null, result = null) }
            try {
                val res = analyzeContentUseCase(
                    AnalysisInput(text = text, tab = _uiState.value.selectedTab)
                )
                _uiState.update { it.copy(isAnalyzing = false, result = res) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        errorMessage = "Error al analizar. Inténtalo de nuevo."
                    )
                }
            } finally {
                isSubmitting = false
            }
        }
    }

    fun clearResult() {
        _uiState.update { it.copy(result = null) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SigafApplication
                AnalyzerViewModel(app.container.analyzeContentUseCase)
            }
        }
    }
}

fun RiskLevel.color(): androidx.compose.ui.graphics.Color = when (this) {
    com.sigaf.app.domain.model.RiskLevel.CRITICAL -> com.sigaf.app.presentation.theme.RiskCritical
    com.sigaf.app.domain.model.RiskLevel.HIGH -> com.sigaf.app.presentation.theme.RiskHigh
    com.sigaf.app.domain.model.RiskLevel.MEDIUM -> com.sigaf.app.presentation.theme.RiskWarning
    com.sigaf.app.domain.model.RiskLevel.LOW -> com.sigaf.app.presentation.theme.RiskSafe
    com.sigaf.app.domain.model.RiskLevel.INSUFFICIENT -> com.sigaf.app.presentation.theme.RiskInsufficient
}
