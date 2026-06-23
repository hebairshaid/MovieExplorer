package com.movieexplorer.movie_details.domain.usecase

import com.movieexplorer.movie_details.domain.repository.MovieDetailRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieDetailRepository
) {
    suspend operator fun invoke(movieId: Int) =
        repository.getMovieDetails(movieId)
}