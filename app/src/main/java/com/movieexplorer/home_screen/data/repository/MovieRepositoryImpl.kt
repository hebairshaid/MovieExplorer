package com.movieexplorer.home_screen.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.movieexplorer.BuildConfig
import com.movieexplorer.home_screen.data.local.MovieDao
import com.movieexplorer.home_screen.data.local.MovieDatabase
import com.movieexplorer.home_screen.data.mapper.toMovie
import com.movieexplorer.home_screen.data.paging.MovieRemoteMediator
import com.movieexplorer.home_screen.data.remote.MovieApi
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.home_screen.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val dao: MovieDao,
    private val database: MovieDatabase
) : MovieRepository {

    override suspend fun getMovieById(movieId: Int): Movie? {
        return dao.getMovieById(movieId)?.toMovie()
    }

    override fun getMovies(genreId: Int): Flow<PagingData<Movie>> {
        println("KEY length: ${BuildConfig.TMDB_API_KEY.length}")

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(api, database, dao, genreId),
            pagingSourceFactory = { dao.getMoviesByGenre(genreId) }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }
    }
}
