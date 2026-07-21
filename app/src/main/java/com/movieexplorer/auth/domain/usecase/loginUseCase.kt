package com.movieexplorer.auth.domain.usecase

import com.movieexplorer.auth.domain.model.User
import com.movieexplorer.auth.domain.repository.AuthRepository
import com.movieexplorer.auth.domain.session.SessionRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(email: String, password: String): User {
        val result = repository.login(email.trim(), password.trim())
        sessionRepository.saveToken(result.token)
        return result.user
    }
}
