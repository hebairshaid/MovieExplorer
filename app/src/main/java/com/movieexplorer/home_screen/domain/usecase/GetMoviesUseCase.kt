package com.movieexplorer.home_screen.domain.usecase

import androidx.paging.PagingData
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.home_screen.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(genreId: Int): Flow<PagingData<Movie>> {
        return repository.getMovies(genreId)
    }

}
/*suspend operator fun invoke(genreId: Int) =
        repository.getMovies(genreId)*/