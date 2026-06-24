package com.movieexplorer.home_screen.data.remote

import com.movieexplorer.home_screen.data.dto.MovieResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import com.movieexplorer.BuildConfig


interface MovieApi {

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("with_genres") genreId: Int
    ): MovieResponseDto
}