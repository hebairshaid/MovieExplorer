package com.movieexplorer.auth.data.local

import com.movieexplorer.authentication.domain.model.User

data class AuthResponse(
    val token: String,
    val user: User
)