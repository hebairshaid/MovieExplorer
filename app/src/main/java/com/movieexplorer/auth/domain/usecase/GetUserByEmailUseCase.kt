package com.movieexplorer.auth.domain.usecase

import com.movieexplorer.authentication.domain.model.User
import com.movieexplorer.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserByEmailUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): User? {
        return repository.getUserByEmail(email)
    }
}
