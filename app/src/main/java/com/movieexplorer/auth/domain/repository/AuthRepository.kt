package com.movieexplorer.authentication.domain.repository

import com.movieexplorer.authentication.domain.model.User

interface AuthRepository {

    suspend fun signUp(user: User)
}