package com.movieexplorer.search_screen.data.repository

import com.movieexplorer.BuildConfig
import com.movieexplorer.home_screen.data.mapper.toMovie
import com.movieexplorer.home_screen.data.remote.MovieApi
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.search_screen.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : SearchRepository {

    override suspend fun searchMovies(query: String): List<Movie> {
        val response = api.searchMovies(
            query = query,
            page = 1,
            apiKey = BuildConfig.TMDB_API_KEY
        )
        return response.results.map { it.toMovie() }
    }
}
