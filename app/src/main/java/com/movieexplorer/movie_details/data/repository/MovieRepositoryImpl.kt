package com.movieexplorer.movie_details.data.repository

import com.movieexplorer.BuildConfig
import com.movieexplorer.home_screen.data.local.MovieDao
import com.movieexplorer.home_screen.data.mapper.toEntity
import com.movieexplorer.home_screen.data.mapper.toMovie
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.movie_details.data.mapper.toDomain
import com.movieexplorer.movie_details.data.remote.MovieDetailApi
import com.movieexplorer.movie_details.domain.repository.MovieDetailRepository
import javax.inject.Inject

class MovieDetailRepositoryImpl @Inject constructor(
    private val api: MovieDetailApi,
    private val dao: MovieDao
) : MovieDetailRepository {

    override suspend fun getMovieDetails(movieId: Int): Movie {
        // Prefer fresh details from API (runtime + correct poster path).
        // Use Room only as offline fallback.
        return try {
            val movie = api.getMovieDetails(
                movieId,
                BuildConfig.TMDB_API_KEY
            ).toDomain()

            dao.insertMovies(listOf(movie.toEntity(genreId = 0)))
            movie
        } catch (e: Exception) {
            val fallback = dao.getMovieById(movieId)?.toMovie()
            fallback ?: throw Exception(e.message ?: "No internet & no cached data")
        }
    }
}
