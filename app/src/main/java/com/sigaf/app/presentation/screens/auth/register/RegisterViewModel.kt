package com.sigaf.app.presentation.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sigaf.app.SigafApplication
import com.sigaf.app.domain.model.AuthResult
import com.sigaf.app.domain.model.RegisterData
import com.sigaf.app.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de UI del Registro.
 */
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptTerms: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel de Registro (MVVM).
 */
class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    sealed interface Event {
        data object RegisterSuccess : Event
    }

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val events: SharedFlow<Event> = _events.asSharedFlow()

    private var isSubmitting = false

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value, errorMessage = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, errorMessage = null) }
    }

    fun onAcceptTermsChange(value: Boolean) {
        _uiState.update { it.copy(acceptTerms = value, errorMessage = null) }
    }

    fun onRegisterClick() {
        if (isSubmitting) return
        val current = _uiState.value

        when {
            current.fullName.isBlank() || current.email.isBlank() || current.password.isBlank() || current.confirmPassword.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Completa todos los campos") }
                return
            }
            current.password != current.confirmPassword -> {
                _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden") }
                return
            }
            current.password.length < 8 -> {
                _uiState.update { it.copy(errorMessage = "La contraseña debe tener al menos 8 caracteres") }
                return
            }
            !current.acceptTerms -> {
                _uiState.update { it.copy(errorMessage = "Debes aceptar los términos y condiciones") }
                return
            }
        }

        isSubmitting = true
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = registerUseCase(
                RegisterData(
                    fullName = current.fullName.trim(),
                    email = current.email.trim(),
                    password = current.password,
                    acceptTerms = current.acceptTerms
                )
            )
            when (result) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(Event.RegisterSuccess)
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
                RegisterViewModel(app.container.registerUseCase)
            }
        }
    }
}
