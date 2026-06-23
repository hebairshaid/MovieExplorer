package com.movieexplorer.home_screen.domain.usecase
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.home_screen.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(genreId: Int) =
        repository.getMovies(genreId)
}