package com.movieexplorer.watchlist_screen.domain.usecase

import com.movieexplorer.watchlist_screen.domain.repository.WatchlistRepository
import javax.inject.Inject

class IsInWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    suspend operator fun invoke(movieId: Int): Boolean {
        return repository.isInWatchlist(movieId)
    }
}

