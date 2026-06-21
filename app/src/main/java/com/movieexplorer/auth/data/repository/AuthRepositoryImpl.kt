package com.movieexplorer.authentication.data.repository

import com.movieexplorer.auth.data.local.AuthResponse
import com.movieexplorer.auth.data.local.UserEntity
import com.movieexplorer.authentication.data.local.UserDao
import com.movieexplorer.authentication.domain.model.User
import com.movieexplorer.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : AuthRepository {

    override suspend fun signUp(user: User) {
        dao.insertUser(          //receive user and convert it userEntity
            UserEntity(
                name = user.name,
                email = user.email,
                password = user.password
            )
        )
    }

    override suspend fun login(email: String, password: String): AuthResponse {

        val user = dao.login(email, password)   // do we have this user
            ?: throw Exception("Invalid credentials")

        return AuthResponse(          //convert database user to domain user to send it back to app
            user = User(
                name = user.name,
                email = user.email,
                password = user.password
            ),
            token = "local_token_${user.email}"
        )
    }

    override suspend fun isEmailExists(email: String): Boolean {  //check email exist used in signup to preventing duplicate accounts
        return dao.getUserByEmail(email) != null
    }
}