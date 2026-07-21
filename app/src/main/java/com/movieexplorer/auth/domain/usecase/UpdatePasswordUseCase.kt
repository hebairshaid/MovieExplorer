package com.movieexplorer.auth.domain.usecase

import com.movieexplorer.auth.domain.model.PasswordPolicy
import com.movieexplorer.auth.domain.repository.AuthRepository
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
        val cleanNew = newPassword.trim()
        if (cleanNew != confirmPassword.trim()) {
            throw Exception("Passwords do not match")
        }
        PasswordPolicy.validate(cleanNew)
        repository.updatePassword(email, currentPassword, cleanNew)
    }
}
