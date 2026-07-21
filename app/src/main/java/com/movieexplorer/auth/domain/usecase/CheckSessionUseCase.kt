package com.movieexplorer.auth.domain.usecase

import com.movieexplorer.auth.domain.session.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    operator fun invoke(): Flow<String?> = sessionRepository.getToken()
}
