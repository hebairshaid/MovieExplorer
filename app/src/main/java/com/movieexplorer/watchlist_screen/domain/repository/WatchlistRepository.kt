package com.movieexplorer.watchlist_screen.domain.repository

import com.movieexplorer.home_screen.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {

    fun observeWatchlist(): Flow<List<Movie>>

    suspend fun isInWatchlist(movieId: Int): Boolean

    suspend fun addToWatchlist(movie: Movie)

    suspend fun removeFromWatchlist(movieId: Int)
}

