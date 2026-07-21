package com.movieexplorer.profile_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.auth.domain.usecase.GetUserByEmailUseCase
import com.movieexplorer.auth.domain.usecase.UpdatePasswordUseCase
import com.movieexplorer.authentication.domain.use_case.CheckSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Ready(
        val name: String,
        val email: String
    ) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val checkSessionUseCase: CheckSessionUseCase,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _currentPassword = MutableStateFlow("")
    val currentPassword = _currentPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private var currentEmail: String = ""

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val token = checkSessionUseCase().first()
                val email = token
                    ?.removePrefix("local_token_")
                    ?.trim()
                    ?.lowercase()
                    .orEmpty()

                if (email.isBlank()) {
                    _uiState.value = ProfileUiState.Error("Not logged in")
                    return@launch
                }

                currentEmail = email
                val user = getUserByEmailUseCase(email)
                if (user == null) {
                    _uiState.value = ProfileUiState.Error("User not found")
                    return@launch
                }

                _uiState.value = ProfileUiState.Ready(
                    name = user.name,
                    email = user.email
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    fun onCurrentPasswordChange(value: String) {
        _currentPassword.value = value
        _message.value = null
    }

    fun onNewPasswordChange(value: String) {
        _newPassword.value = value
        _message.value = null
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
        _message.value = null
    }

    fun savePassword() {
        viewModelScope.launch {
            try {
                updatePasswordUseCase(
                    email = currentEmail,
                    currentPassword = _currentPassword.value.trim(),
                    newPassword = _newPassword.value.trim(),
                    confirmPassword = _confirmPassword.value.trim()
                )
                _currentPassword.value = ""
                _newPassword.value = ""
                _confirmPassword.value = ""
                _message.value = "Password updated successfully"
            } catch (e: Exception) {
                _message.value = e.message ?: "Failed to update password"
            }
        }
    }
}
