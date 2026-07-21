package com.movieexplorer.auth.domain.usecase

import com.movieexplorer.auth.domain.model.User
import com.movieexplorer.auth.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserByEmailUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): User? {
        return repository.getUserByEmail(email.trim().lowercase())
    }
}
