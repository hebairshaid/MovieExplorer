package com.movieexplorer.home_screen.domain.repository

import com.movieexplorer.home_screen.domain.model.Movie

interface MovieRepository {
    suspend fun getMovies(genreId: Int): List<Movie>
}