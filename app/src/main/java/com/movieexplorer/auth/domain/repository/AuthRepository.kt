package com.movieexplorer.auth.domain.repository

import com.movieexplorer.auth.domain.model.AuthResponse
import com.movieexplorer.auth.domain.model.User

interface AuthRepository {
    suspend fun signUp(user: User)

    suspend fun login(email: String, password: String): AuthResponse

    suspend fun isEmailExists(email: String): Boolean

    suspend fun getUserByEmail(email: String): User?

    suspend fun updatePassword(
        email: String,
        currentPassword: String,
        newPassword: String
    )
}
