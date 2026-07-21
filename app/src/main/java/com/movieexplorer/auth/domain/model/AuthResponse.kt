package com.movieexplorer.auth.domain.model

data class AuthResponse(
    val token: String,
    val user: User
)
