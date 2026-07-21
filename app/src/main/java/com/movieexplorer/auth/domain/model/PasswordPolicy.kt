package com.movieexplorer.auth.domain.model

object PasswordPolicy {

    fun validate(password: String) {
        if (password.length < 6) {
            throw Exception("Password must be at least 6 characters")
        }
        if (!password.any { it.isUpperCase() }) {
            throw Exception("Password must contain a capital letter")
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            throw Exception("Password must contain a special character")
        }
    }
}
