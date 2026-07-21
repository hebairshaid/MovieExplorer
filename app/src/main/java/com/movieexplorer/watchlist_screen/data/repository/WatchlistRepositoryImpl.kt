package com.movieexplorer.watchlist_screen.data.repository

import com.movieexplorer.auth.domain.session.SessionRepository
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.watchlist_screen.data.local.WatchlistDao
import com.movieexplorer.watchlist_screen.data.mapper.toMovie
import com.movieexplorer.watchlist_screen.data.mapper.toWatchlistEntity
import com.movieexplorer.watchlist_screen.domain.repository.WatchlistRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class WatchlistRepositoryImpl @Inject constructor(
    private val dao: WatchlistDao,
    private val sessionRepository: SessionRepository
) : WatchlistRepository {

    override fun observeWatchlist(): Flow<List<Movie>> {
        return sessionRepository.getToken().flatMapLatest { token ->
            val email = emailFromToken(token)
            if (email.isBlank()) {
                flowOf(emptyList())
            } else {
                dao.observeWatchlist(email).map { list ->
                    list.map { it.toMovie() }
                }
            }
        }
    }

    override suspend fun isInWatchlist(movieId: Int): Boolean {
        val email = requireUserEmail()
        return dao.watchlistCount(email, movieId) > 0
    }

    override suspend fun addToWatchlist(movie: Movie) {
        val email = requireUserEmail()
        dao.addMovie(movie.toWatchlistEntity(email))
    }

    override suspend fun removeFromWatchlist(movieId: Int) {
        val email = requireUserEmail()
        dao.removeMovie(email, movieId)
    }

    private suspend fun requireUserEmail(): String {
        val email = emailFromToken(sessionRepository.getToken().first())
        if (email.isBlank()) {
            throw Exception("Not logged in")
        }
        return email
    }

    private fun emailFromToken(token: String?): String {
        return token
            ?.removePrefix("local_token_")
            ?.trim()
            ?.lowercase()
            .orEmpty()
    }
}
