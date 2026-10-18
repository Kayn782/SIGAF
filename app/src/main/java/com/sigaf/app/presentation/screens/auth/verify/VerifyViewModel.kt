package com.sigaf.app.presentation.screens.auth.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sigaf.app.SigafApplication
import com.sigaf.app.domain.model.AuthResult
import com.sigaf.app.domain.model.VerificationCode
import com.sigaf.app.domain.usecase.auth.ResendCodeUseCase
import com.sigaf.app.domain.usecase.auth.VerifyCodeUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de UI de Verificación de Correo.
 */
data class VerifyUiState(
    val codeDigits: List<String> = listOf("", "", "", ""),
    val isLoading: Boolean = false,
    val isResending: Boolean = false,
    val errorMessage: String? = null,
    val timeLeftSeconds: Int = 42,
    val canResend: Boolean = false
)

/**
 * ViewModel de Verificación (MVVM).
 */
class VerifyViewModel(
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val resendCodeUseCase: ResendCodeUseCase
) : ViewModel() {

    sealed interface Event {
        data object VerifySuccess : Event
        data object CodeResent : Event
    }

    private val _uiState = MutableStateFlow(VerifyUiState())
    val uiState: StateFlow<VerifyUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val events: SharedFlow<Event> = _events.asSharedFlow()

    private var isSubmitting = false
    private var countdownActive = true

    init {
        startCountdown()
    }

    private fun startCountdown() {
        countdownActive = true
        viewModelScope.launch {
            var seconds = 42
            while (seconds > 0 && countdownActive) {
                _uiState.update { it.copy(timeLeftSeconds = seconds, canResend = false) }
                delay(1000)
                seconds--
            }
            countdownActive = false
            _uiState.update { it.copy(timeLeftSeconds = 0, canResend = true) }
        }
    }

    fun onDigitChange(index: Int, value: String) {
        if (value.length > 1) return
        val newDigits = _uiState.value.codeDigits.toMutableList()
        newDigits[index] = value.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                codeDigits = newDigits,
                errorMessage = null
            )
        }

        // Auto-avanzar al siguiente dígito si se completó
        val codeFilled = newDigits.all { it.isNotBlank() }
        if (codeFilled && !isSubmitting) {
            onVerifyClick()
        }
    }

    fun onResendClick() {
        if (_uiState.value.isResending || !(_uiState.value.canResend || _uiState.value.timeLeftSeconds == 0)) return
        viewModelScope.launch {
            _uiState.update { it.copy(isResending = true, errorMessage = null) }
            when (resendCodeUseCase()) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(isResending = false) }
                    _events.emit(Event.CodeResent)
                    // Reiniciar contador
                    countdownActive = false
                    startCountdown()
                }
                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isResending = false,
                            errorMessage = "No se pudo reenviar el código"
                        )
                    }
                }
            }
        }
    }

    fun onVerifyClick() {
        if (isSubmitting) return
        val currentDigits = _uiState.value.codeDigits
        if (currentDigits.any { it.isBlank() }) {
            _uiState.update { it.copy(errorMessage = "Ingresa el código completo") }
            return
        }
        isSubmitting = true
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val codeString = currentDigits.joinToString("")
            val result = verifyCodeUseCase(VerificationCode(code = codeString))
            when (result) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(Event.VerifySuccess)
                }
                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    isSubmitting = false
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SigafApplication
                VerifyViewModel(
                    verifyCodeUseCase = app.container.verifyCodeUseCase,
                    resendCodeUseCase = app.container.resendCodeUseCase
                )
            }
        }
    }
}
