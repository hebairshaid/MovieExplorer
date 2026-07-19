package com.movieexplorer.search_screen.domain.usecase

import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.search_screen.domain.repository.SearchRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): List<Movie> {
        return repository.searchMovies(query.trim())
    }
}
