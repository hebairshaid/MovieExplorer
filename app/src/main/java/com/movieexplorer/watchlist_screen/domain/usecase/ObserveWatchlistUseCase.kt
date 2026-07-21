package com.movieexplorer.watchlist_screen.domain.usecase

import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.watchlist_screen.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    operator fun invoke(): Flow<List<Movie>> = repository.observeWatchlist()
}

