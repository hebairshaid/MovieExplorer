package com.movieexplorer.auth.domain.usecase

import com.movieexplorer.auth.domain.session.SessionRepository

class LogoutUseCase(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke() {
        sessionRepository.clearSession()
    }
}
//removes the user’s login session by clearing the stored token