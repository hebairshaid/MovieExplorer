package com.movieexplorer.movie_details.domain.repository

import com.movieexplorer.home_screen.domain.model.Movie

interface MovieDetailRepository {

    suspend fun getMovieDetails(movieId: Int): Movie
}