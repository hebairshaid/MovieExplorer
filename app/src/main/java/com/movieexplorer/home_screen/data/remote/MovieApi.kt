package com.movieexplorer.home_screen.data.remote

import com.movieexplorer.home_screen.data.dto.MovieResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String, @Query("with_genres") genreId: Int
    ): MovieResponseDto
}