package com.movieexplorer.movie_details.data.repository

import com.movieexplorer.movie_details.data.remote.MovieDetailApi
import com.movieexplorer.movie_details.data.mapper.toDomain
import com.movieexplorer.movie_details.domain.repository.MovieDetailRepository
import com.movieexplorer.home_screen.domain.model.Movie
import javax.inject.Inject
import com.movieexplorer.BuildConfig
import com.movieexplorer.home_screen.data.local.MovieDao
//import com.movieexplorer.home_screen.data.mapper.toDetailEntity
import com.movieexplorer.home_screen.data.mapper.toMovie
import com.movieexplorer.home_screen.data.mapper.toEntity

class MovieDetailRepositoryImpl @Inject constructor(
    private val api: MovieDetailApi,
    private val dao: MovieDao
) : MovieDetailRepository {

    //private val apiKey = "1da330dbe24c8b172c9a89f3dfe342db"

    override suspend fun getMovieDetails(movieId: Int): Movie {

        val cached = dao.getMovieById(movieId)
        if (cached != null) {
            return cached.toMovie()
        }

        return try {
            val response = api.getMovieDetails(
                movieId,
                BuildConfig.TMDB_API_KEY
            )
            //return response.toDomain()
            val movie = response.toDomain()

            // 3. SAVE TO CACHE (IMPORTANT)
            dao.insertMovies(listOf(movie.toEntity(genreId = 0)))

            movie

        } catch (e: Exception) {

            // offline fallback
            val fallback = dao.getMovieById(movieId)

            if (fallback != null) {
                fallback.toMovie()
            } else {
                throw Exception("No internet & no cached data")
            }


            /*  private val apiKey = "1da330dbe24c8b172c9a89f3dfe342db"

              override suspend fun getMovieDetails(movieId: Int): Movie {
                  val response = api.getMovieDetails(movieId, apiKey)
                  return response.toDomain()
              }*/
        }}}