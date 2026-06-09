package com.movieexplorer.authentication.data.repository

import com.movieexplorer.auth.data.local.UserEntity
import com.movieexplorer.authentication.data.local.UserDao
import com.movieexplorer.authentication.domain.model.User
import com.movieexplorer.authentication.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val dao: UserDao
) : AuthRepository {

    override suspend fun signUp(user: User) {

        val existingUser = dao.getUserByEmail(user.email)

        if (existingUser != null) {
            throw Exception("Email already exists")
        }

        dao.insertUser(
            UserEntity(
                name = user.name,
                email = user.email,
                password = user.password
            )
        )
    }
}