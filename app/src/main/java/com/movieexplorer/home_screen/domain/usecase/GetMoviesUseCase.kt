package com.movieexplorer.home_screen.domain.usecase
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.home_screen.domain.repository.MovieRepository

class GetMoviesUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        genreId: Int
    ): List<Movie> {
        return repository.getMovies(genreId)
    }
}