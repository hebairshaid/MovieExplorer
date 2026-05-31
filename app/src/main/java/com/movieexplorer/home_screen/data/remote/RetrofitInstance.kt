package com.movieexplorer.data.remote

import com.movieexplorer.home_screen.data.remote.MovieApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

    val api: MovieApi =
        retrofit.create(MovieApi::class.java)
}