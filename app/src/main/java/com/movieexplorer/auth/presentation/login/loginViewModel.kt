package com.movieexplorer.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.auth.domain.usecase.LoginUseCase
import com.movieexplorer.authentication.presentation.login.LoginState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onEmailChange(value: String) {
        state = state.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value)
    }

    fun login() {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true, error = null)

                loginUseCase(state.email, state.password)

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