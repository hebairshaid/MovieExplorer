package com.movieexplorer.authentication.domain.model

data class User(  // a container for user info
    val name: String,
    val email: String,
    val password: String,
)
// just clean version used in business logic used in sign up ,login response and in view model UI