package com.movieexplorer.auth.domain.usecase

import com.movieexplorer.auth.domain.model.User
import com.movieexplorer.auth.domain.model.PasswordPolicy
import com.movieexplorer.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(user: User) {
        if (user.name.isBlank()) {
            throw Exception("Name is required")
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        if (!emailRegex.matches(user.email)) {
            throw Exception("Invalid email format")
        }

        if (repository.isEmailExists(user.email)) {
            throw Exception("Email already exists")
        }

        PasswordPolicy.validate(user.password)
        repository.signUp(user)
    }
}
