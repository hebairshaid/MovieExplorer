package com.movieexplorer.authentication.presentation.signup

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.auth.presentation.signup.SignUpUiState
import com.movieexplorer.authentication.domain.model.User
import com.movieexplorer.authentication.domain.use_case.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    /*var state by mutableStateOf(SignUpState())
        private set*/
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



    /*fun onNameChange(value: String) {
        state = state.copy(name = value)
    }*/
    fun onNameChange(value: String) {
        _name.value = value
    }

    /*fun onEmailChange(value: String) {
        state = state.copy(email = value)
    }*/
    fun onEmailChange(value: String) {
        _email.value = value
    }

    /*fun onPasswordChange(value: String) {
        state = state.copy(password = value)
    }*/
    fun onPasswordChange(value: String) {
        _password.value = value
    }


    /*fun onConfirmPasswordChange(value: String) {
        state = state.copy(confirmPassword = value)
    }*/
    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
    }


    fun signUp() {

        if (password != confirmPassword) {
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
                        password = _password.value
                    )
                )

                _state.value = SignUpUiState.Success("Account created successfully")

            } catch (e: Exception) {

                _state.value = SignUpUiState.Error(
                    e.message ?: "Sign up failed"
                )
            }
        }
    }
}

    /*fun signUp() {
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
        }*/

