package com.movieexplorer.authentication.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
/*
LoginState is a single object that contains everything the Login screen needs to know:
the user's email, password, loading status, error message, and whether login was successful
*/