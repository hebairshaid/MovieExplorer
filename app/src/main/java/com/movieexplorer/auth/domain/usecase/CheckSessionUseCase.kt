package com.movieexplorer.authentication.domain.use_case

import com.movieexplorer.auth.domain.session.SessionRepository
import kotlinx.coroutines.flow.Flow

class CheckSessionUseCase(
    private val sessionRepository: SessionRepository
) {
    operator fun invoke(): Flow<String?> { //invoke allow you to call the class like a function instead of write checkSessionUseCase.invoke() you can write checkSessionUseCase
        return sessionRepository.getToken() //give me the saved token return token if logged in or null if not logged in
    }
}
/*
usecase is specific action in your app is the business logic
*/