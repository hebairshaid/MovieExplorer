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

        val user = dao.getUserByEmail(cleanEmail)
            ?: throw Exception("Invalid credentials")

        // verify password using hasher
        val isPasswordCorrect = passwordHasher.verify(password, user.password)

        //before the DB is compare now the kotlin check

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
        return dao.getUserByEmail(email) != null
    }
}