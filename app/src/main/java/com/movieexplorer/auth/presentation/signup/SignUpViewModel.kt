package com.movieexplorer.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.auth.domain.model.User
import com.movieexplorer.auth.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val state: StateFlow<SignUpUiState> = _state.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    fun onNameChange(value: String) {
        _name.value = value
    }

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
    }

    fun signUp() {
        if (_password.value != _confirmPassword.value) {
            _state.value = SignUpUiState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _state.value = SignUpUiState.Loading
            try {
                signUpUseCase(
                    User(
                        name = _name.value.trim(),
                        email = _email.value.trim().lowercase(),
                        password = _password.value.trim()
                    )
                )
                _state.value = SignUpUiState.Success("Account created successfully")
            } catch (e: Exception) {
                _state.value = SignUpUiState.Error(e.message ?: "Sign up failed")
            }
        }
    }
}
