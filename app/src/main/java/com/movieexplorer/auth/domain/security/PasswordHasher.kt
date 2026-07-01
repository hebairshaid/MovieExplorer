package com.movieexplorer.auth.domain.security

interface PasswordHasher {
    fun hash(password: String): String

    fun verify(plainPassword: String, hashedPassword: String): Boolean
}