package com.movieexplorer.movie_details.domain.usecase

import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.movie_details.domain.repository.MovieDetailRepository

class GetMovieDetailsUseCase(
    private val repository: MovieDetailRepository
) {
    suspend operator fun invoke(movieId: Int) =
        repository.getMovieDetails(movieId)
}