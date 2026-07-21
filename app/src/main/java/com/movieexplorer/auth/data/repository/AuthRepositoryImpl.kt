package com.movieexplorer.auth.data.repository

import com.movieexplorer.auth.data.local.UserEntity
import com.movieexplorer.auth.data.local.UserDao
import com.movieexplorer.auth.domain.model.AuthResponse
import com.movieexplorer.auth.domain.model.User
import com.movieexplorer.auth.domain.repository.AuthRepository
import com.movieexplorer.auth.domain.security.PasswordHasher
import com.movieexplorer.auth.domain.session.SessionToken
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val passwordHasher: PasswordHasher
) : AuthRepository {

    override suspend fun signUp(user: User) {
        val cleanEmail = user.email.trim().lowercase()
        val hashedUser = user.copy(
            email = cleanEmail,
            password = passwordHasher.hash(user.password.trim())
        )

        dao.insertUser(
            UserEntity(
                name = hashedUser.name.trim(),
                email = hashedUser.email,
                password = hashedUser.password
            )
        )
    }

    override suspend fun login(email: String, password: String): AuthResponse {
        val cleanEmail = email.trim().lowercase()
        val cleanPassword = password.trim()

        val user = dao.getUserByEmail(cleanEmail)
            ?: throw Exception("Invalid credentials")

        if (!passwordHasher.verify(cleanPassword, user.password)) {
            throw Exception("Invalid credentials")
        }

        return AuthResponse(
            user = User(
                name = user.name,
                email = user.email,
                password = user.password
            ),
            token = SessionToken.create(user.email)
        )
    }

    override suspend fun isEmailExists(email: String): Boolean {
        return dao.getUserByEmail(email.trim().lowercase()) != null
    }

    override suspend fun getUserByEmail(email: String): User? {
        val entity = dao.getUserByEmail(email.trim().lowercase()) ?: return null
        return User(
            name = entity.name,
            email = entity.email,
            password = entity.password
        )
    }

    override suspend fun updatePassword(
        email: String,
        currentPassword: String,
        newPassword: String
    ) {
        val cleanEmail = email.trim().lowercase()
        val cleanCurrent = currentPassword.trim()
        val cleanNew = newPassword.trim()

        val user = dao.getUserByEmail(cleanEmail)
            ?: throw Exception("User not found")

        if (!passwordHasher.verify(cleanCurrent, user.password)) {
            throw Exception("Current password is incorrect")
        }

        val hashed = passwordHasher.hash(cleanNew)
        val byId = dao.updatePasswordById(userId = user.id, hashedPassword = hashed)
        val byEmail = dao.updatePasswordByEmail(email = cleanEmail, hashedPassword = hashed)

        if (byId == 0 && byEmail == 0) {
            throw Exception("Failed to update password")
        }

        val refreshed = dao.getUserByEmail(cleanEmail)
            ?: throw Exception("Failed to update password")

        if (!passwordHasher.verify(cleanNew, refreshed.password)) {
            throw Exception("Password update failed verification")
        }
    }
}
