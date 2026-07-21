package com.movieexplorer.auth.domain.usecase

import com.movieexplorer.auth.domain.model.User
import com.movieexplorer.auth.domain.repository.AuthRepository
import com.movieexplorer.auth.domain.session.SessionRepository
import com.movieexplorer.auth.domain.session.SessionToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): User? {
        val email = SessionToken.emailFrom(sessionRepository.getToken().first())
        if (email.isBlank()) return null
        return authRepository.getUserByEmail(email)
    }
}
