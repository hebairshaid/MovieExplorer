package com.movieexplorer.auth.presentation.signup

sealed interface SignUpUiState {

    data object Idle : SignUpUiState // Idle = initial state Screen is opened but nothing has happened yet

    data object Loading : SignUpUiState

    data class Success(val message: String) : SignUpUiState

    data class Error(val message: String) : SignUpUiState
}