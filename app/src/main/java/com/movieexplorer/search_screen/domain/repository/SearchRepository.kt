package com.movieexplorer.search_screen.domain.repository

import com.movieexplorer.home_screen.domain.model.Movie

interface SearchRepository {
    suspend fun searchMovies(query: String): List<Movie>
}
