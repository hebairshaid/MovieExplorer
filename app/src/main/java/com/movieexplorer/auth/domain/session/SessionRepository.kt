package com.movieexplorer.auth.domain.session

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getToken(): Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun clearSession()
}
