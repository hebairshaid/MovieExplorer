package com.movieexplorer.home_screen.data.repository

import com.movieexplorer.home_screen.data.dto.MovieDto
import com.movieexplorer.home_screen.data.mapper.toMovie
import com.movieexplorer.home_screen.data.remote.MovieApi
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.home_screen.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : MovieRepository {

    override suspend fun getMovies(genreId: Int): List<Movie> {

        return api.getMovies(
            apiKey = "1da330dbe24c8b172c9a89f3dfe342db",
            genreId = genreId
        ).results.map { dto: MovieDto ->
            dto.toMovie()
        }
    }
}