package com.movieexplorer.movie_details.data.repository

import com.movieexplorer.movie_details.data.remote.MovieDetailApi
import com.movieexplorer.movie_details.data.mapper.toDomain
import com.movieexplorer.movie_details.domain.repository.MovieDetailRepository
import com.movieexplorer.home_screen.domain.model.Movie
import javax.inject.Inject

class MovieDetailRepositoryImpl @Inject constructor(
    private val api: MovieDetailApi
) : MovieDetailRepository {

    private val apiKey = "1da330dbe24c8b172c9a89f3dfe342db"

    override suspend fun getMovieDetails(movieId: Int): Movie {
        val response = api.getMovieDetails(movieId, apiKey)
        return response.toDomain()
    }
}