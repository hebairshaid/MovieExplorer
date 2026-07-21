package com.movieexplorer.auth.domain.usecase

import com.movieexplorer.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        if (newPassword.trim() != confirmPassword.trim()) {
            throw Exception("Passwords do not match")
        }
        repository.updatePassword(email, currentPassword, newPassword)
    }
}
