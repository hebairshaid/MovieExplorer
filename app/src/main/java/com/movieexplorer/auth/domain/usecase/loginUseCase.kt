package com.movieexplorer.auth.domain.usecase

import com.movieexplorer.auth.domain.session.SessionRepository
import com.movieexplorer.authentication.domain.model.User
import com.movieexplorer.authentication.domain.repository.AuthRepository

class LoginUseCase(  //logs user in and save the session
    private val repository: AuthRepository,  //to check login credentials create an object instance
    private val sessionRepository: SessionRepository //to save login token
) {

    suspend operator fun invoke(email: String, password: String): User { //allow call as a function

        val result = repository.login(email, password) //check database   val result = repository.login(email, password) and return authResponse

        sessionRepository.saveToken(result.token) //take token from login and save it

        return result.user //we need user info so return it
    }
}
/*
AuthRepository → checks if you are a valid guest
SessionRepository → gives you a room key (token)
UseCase → handles the whole check-in process
*/