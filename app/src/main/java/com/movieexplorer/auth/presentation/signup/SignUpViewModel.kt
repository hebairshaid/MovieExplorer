package com.movieexplorer.authentication.presentation.signup

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.authentication.domain.model.User
import com.movieexplorer.authentication.domain.use_case.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    var state by mutableStateOf(SignUpState())
        private set

    fun onNameChange(value: String) {
        state = state.copy(name = value)
    }

    fun onEmailChange(value: String) {
        state = state.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value)
    }

    fun onConfirmPasswordChange(value: String) {
        state = state.copy(confirmPassword = value)
    }

    fun signUp() {
        if (state.password != state.confirmPassword) {
            state = state.copy(error = "Passwords do not match")
            return
        }
        viewModelScope.launch {

            try {
                state = state.copy(isLoading = true, error = null, success = false)

                signUpUseCase(
                    User(
                        name = state.name.trim(),
                        email = state.email.trim().lowercase(),
                        password = state.password
                    )
                )

                state = state.copy(
                    isLoading = false,
                    success = true
                )

            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}