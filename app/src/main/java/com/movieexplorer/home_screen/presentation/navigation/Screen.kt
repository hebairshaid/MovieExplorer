package com.movieexplorer.home_screen.presentation.navigation

object Screen {

    const val Home = "home"

    const val MovieDetails = "movie_details/{movieId}"

    fun movieDetailsScreen(movieId: Int): String {
        return "movie_details/$movieId"
    }
}