package com.movieexplorer.home_screen.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.movieexplorer.home_screen.data.paging.MoviePagingSource
import com.movieexplorer.home_screen.data.remote.MovieApi
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.home_screen.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.movieexplorer.BuildConfig
import com.movieexplorer.home_screen.data.paging.MovieRemoteMediator
import com.movieexplorer.home_screen.data.local.MovieDao
import androidx.paging.map
import com.movieexplorer.home_screen.data.mapper.toMovie
import kotlinx.coroutines.flow.map

@OptIn(androidx.paging.ExperimentalPagingApi::class)
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val dao: MovieDao
) : MovieRepository {

    //override suspend fun getMovies(genreId: Int): List<Movie> {
    override suspend fun getMovieById(movieId: Int): Movie? {
        return dao.getMovieById(movieId)?.toMovie()
    }

    override fun getMovies(genreId: Int): Flow<PagingData<Movie>> {
        println("KEY: ${BuildConfig.TMDB_API_KEY}")

        val pagingSourceFactory = {
            dao.getMoviesByGenre(genreId)
        }

        return Pager( //This is the engine of Paging 3
            //It controls: when to load page 1 when to load page 2 caching retry flow emission
            config = PagingConfig( //Load 20 movies per page
                pageSize = 20,     //this must match TMDb default page size
                enablePlaceholders = false //Don’t show empty fake items while loading
            ),
            remoteMediator = MovieRemoteMediator(api, dao, genreId),
           /* pagingSourceFactory = {  //When you need data, create a MoviePagingSource
                MoviePagingSource(api, genreId)
            }*///A new instance is created when needed Each genre has its own PagingSource It calls your API internally

            pagingSourceFactory = {
                androidx.paging.PagingSourceFactory {
                    dao.getMoviesByGenre(genreId)
                }.invoke()
            }
            ).flow.map { pagingData ->
            pagingData.map { it.toMovie() } //This converts Pager into Flow<PagingData<Movie>>
        //Meaning: UI will observe it updates automatically loads more when scrolling



    }
}
}

       /* return api.getMovies(
            //apiKey = "1da330dbe24c8b172c9a89f3dfe342db",
            apiKey = BuildConfig.TMDB_API_KEY,
            genreId = genreId
        ).results.map { dto: MovieDto ->
            dto.toMovie()
        }*/
