package com.movieexplorer.home_screen.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.movieexplorer.home_screen.data.local.CachedMovieEntity
import com.movieexplorer.home_screen.data.local.MovieDao
import com.movieexplorer.home_screen.data.mapper.toEntity
import com.movieexplorer.home_screen.data.mapper.toMovie
import com.movieexplorer.home_screen.data.remote.MovieApi

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val api: MovieApi,
    private val dao: MovieDao,
    private val genreId: Int
) : RemoteMediator<Int, CachedMovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CachedMovieEntity>
    ): MediatorResult {

        return try {

            val page = when (loadType) {

                LoadType.REFRESH -> {
                    dao.clearGenre(genreId)
                    1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    if (lastItem == null) {
                        1
                    } else {
                        (state.pages.sumOf { it.data.size } / 20) + 1
                    }
                }
            }

            println("REMOTE MEDIATOR page = $page genre = $genreId")

            val response = api.getMovies(
                genreId = genreId,
                page = page
            )

            println("Movies from API = ${response.results.size}")

            val movies = response.results.map {
                it.toMovie().toEntity(genreId)
            }

            println("Inserting into Room")

            dao.insertMovies(movies)

            println("Inserted successfully")
            println("ROOM COUNT = ${dao.countMovies()}")

            MediatorResult.Success(
                endOfPaginationReached = response.results.isEmpty()
            )

        } catch (e: Exception) {

            println("REMOTE MEDIATOR ERROR = ${e.message}")

            MediatorResult.Error(e)
        }
    }
}