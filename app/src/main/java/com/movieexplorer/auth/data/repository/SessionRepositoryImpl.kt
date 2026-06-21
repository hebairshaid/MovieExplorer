package com.movieexplorer.auth.data.repository

import com.movieexplorer.auth.data.local.SessionManager
import com.movieexplorer.auth.domain.session.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor (  //layer between app logic(view model) and dataStore (a real implementation of session logic
    private val sessionManager: SessionManager
) : SessionRepository {

    override fun getToken(): Flow<String?> {  // give me the saved token
        return sessionManager.getToken()  // return it Flow which mean a live data that update automatically
    }

    override suspend fun saveToken(token: String) {  // store token in dataStore
        sessionManager.saveToken(token)
    }

    override suspend fun clearSession() {  //used when user logs out to delete session
        sessionManager.clearToken()
    }
}