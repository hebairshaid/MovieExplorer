package com.movieexplorer.auth.presentation.login

sealed interface LoginUiState {

    data object Idle : LoginUiState

    data object Loading : LoginUiState

    data class Success(
        val message: String = "Login successful"
    ) : LoginUiState

    data class Error(
        val message: String
    ) : LoginUiState
}
/*Use Idle when:

Screen has user input (forms)
Login
SignUp
Profile edit

 Because screen starts waiting for user action*/