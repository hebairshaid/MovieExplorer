package com.movieexplorer.authentication.domain.model

data class User(
    val name: String,
    val email: String,
    val password: String
)