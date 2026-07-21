package com.movieexplorer.watchlist_screen.domain.usecase

import com.movieexplorer.watchlist_screen.domain.repository.WatchlistRepository
import javax.inject.Inject

class RemoveFromWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    suspend operator fun invoke(movieId: Int) {
        repository.removeFromWatchlist(movieId)
    }
}

