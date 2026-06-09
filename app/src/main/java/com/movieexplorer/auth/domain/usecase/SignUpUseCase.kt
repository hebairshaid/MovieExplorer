package com.movieexplorer.authentication.domain.use_case

import com.movieexplorer.authentication.domain.model.User
import com.movieexplorer.authentication.domain.repository.AuthRepository

class SignUpUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(user: User) {

        if (user.name.isBlank()) {
            throw Exception("Name is required")
        }

        if (user.email.isBlank()) {
            throw Exception("Email is required")
        }

        if (user.password.length < 6) {
            throw Exception("Password must be at least 6 characters")
        }

        repository.signUp(user)
    }
}