package com.movieexplorer.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
        if (_state.value is LoginUiState.Error) {
            _state.value = LoginUiState.Idle
        }
    }

    fun onPasswordChange(value: String) {
        _password.value = value
        if (_state.value is LoginUiState.Error) {
            _state.value = LoginUiState.Idle
        }
    }

    fun clearError() {
        if (_state.value is LoginUiState.Error) {
            _state.value = LoginUiState.Idle
        }
    }

    fun login() {
        viewModelScope.launch {
            _state.value = LoginUiState.Loading
            try {
                val user = loginUseCase(
                    _email.value.trim(),
                    _password.value.trim()
                )
                _state.value = LoginUiState.Success(
                    message = "Welcome ${user.name}"
                )
            } catch (e: Exception) {
                _state.value = LoginUiState.Error(
                    message = e.message ?: "Login failed"
                )
            }
        }
    }
}
