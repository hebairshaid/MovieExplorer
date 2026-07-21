package com.movieexplorer.auth.data.repository

import com.movieexplorer.auth.data.security.SecureSessionManager
import com.movieexplorer.auth.domain.session.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionManager: SecureSessionManager
) : SessionRepository {

    override fun getToken(): Flow<String?> = sessionManager.getToken()

    override suspend fun saveToken(token: String) {
        sessionManager.saveToken(token)
    }

    override suspend fun clearSession() {
        sessionManager.clearToken()
    }
}
