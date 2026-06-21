package com.movieexplorer.authentication.domain.repository

import com.movieexplorer.auth.data.local.AuthResponse
import com.movieexplorer.authentication.domain.model.User
import javax.inject.Inject

interface AuthRepository { //define what app must do not how to do
                           //any class that implements it MUST follow these rules
    suspend fun signUp(user: User)

    suspend fun login(
        email: String,
        password: String
    ): AuthResponse

    suspend fun isEmailExists(email: String): Boolean
}
/*
interface=what should be done  define the action what function must be exist
implementation= how it is done implements the real logic
* */