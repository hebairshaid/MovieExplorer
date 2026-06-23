package com.movieexplorer.navigation

object Screen {

    const val Splash = "splash"
    const val Login = "login"
    const val SignUp = "signup"
    const val Home = "home"

    const val MovieDetails = "movie_details/{movieId}"

    fun movieDetailsScreen(movieId: Int): String {
        return "movie_details/$movieId"
    }
}