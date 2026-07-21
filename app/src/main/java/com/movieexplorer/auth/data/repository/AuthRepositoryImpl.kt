package com.movieexplorer.authentication.data.repository

import com.movieexplorer.auth.data.local.AuthResponse
import com.movieexplorer.auth.data.local.UserEntity
import com.movieexplorer.auth.domain.security.PasswordHasher
import com.movieexplorer.authentication.data.local.UserDao
import com.movieexplorer.authentication.domain.model.User
import com.movieexplorer.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val passwordHasher: PasswordHasher
) : AuthRepository {

    override suspend fun signUp(user: User) {

        val cleanEmail = user.email.trim().lowercase()

        val hashedUser = user.copy(  //user.copy() → creates a NEW User object (does NOT change original)
            email = cleanEmail,
            password = passwordHasher.hash(user.password) //passwordHasher.hash(...) → converts password into a secure hashed version
        )//Take the user and replace their password with a hashed version

        dao.insertUser(
            UserEntity(
                name = hashedUser.name,
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

        val isPasswordCorrect = passwordHasher.verify(cleanPassword, user.password)

        if (!isPasswordCorrect) {
            throw Exception("Invalid credentials")
        }

        return AuthResponse(
            user = User(
                name = user.name,
                email = user.email,
                password = user.password
            ),
            token = "local_token_${user.email}"
        )
    }

    /*
passwordHasher.verify(...)

This function:

✔ takes the raw password
✔ hashes it internally
✔ compares it with stored hash

So internally it does something like:

hash(inputPassword) == storedHashedPassword

BUT safely (because real hash libraries don’t just compare strings).
*/


    override suspend fun isEmailExists(email: String): Boolean {  //check email exist used in signup to preventing duplicate accounts
        val cleanEmail = email.trim().lowercase()
        return dao.getUserByEmail(cleanEmail) != null
    }

    override suspend fun getUserByEmail(email: String): User? {
        val cleanEmail = email.trim().lowercase()
        val entity = dao.getUserByEmail(cleanEmail) ?: return null
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

        if (cleanNew.length < 6) {
            throw Exception("Password must be at least 6 characters")
        }

        if (!cleanNew.any { it.isUpperCase() }) {
            throw Exception("Password must contain a capital letter")
        }

        if (!cleanNew.any { !it.isLetterOrDigit() }) {
            throw Exception("Password must contain a special character")
        }

        val hashed = passwordHasher.hash(cleanNew)

        // Update by id and by email so every matching row stays in sync
        val byId = dao.updatePasswordById(userId = user.id, hashedPassword = hashed)
        val byEmail = dao.updatePasswordByEmail(email = cleanEmail, hashedPassword = hashed)

        if (byId == 0 && byEmail == 0) {
            throw Exception("Failed to update password")
        }

        // Confirm the new password can log in before telling the user it succeeded
        val refreshed = dao.getUserByEmail(cleanEmail)
            ?: throw Exception("Failed to update password")

        if (!passwordHasher.verify(cleanNew, refreshed.password)) {
            throw Exception("Password update failed verification")
        }
    }
}