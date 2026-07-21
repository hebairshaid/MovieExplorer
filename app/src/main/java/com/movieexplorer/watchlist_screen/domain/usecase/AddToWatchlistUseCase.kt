package com.movieexplorer.watchlist_screen.domain.usecase

import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.watchlist_screen.domain.repository.WatchlistRepository
import javax.inject.Inject

class AddToWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.addToWatchlist(movie)
    }
}

