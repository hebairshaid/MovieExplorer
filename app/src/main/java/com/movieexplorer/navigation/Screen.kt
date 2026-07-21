package com.movieexplorer.navigation

object Screen {

    const val Splash = "splash"
    const val Login = "login"
    const val SignUp = "signup"
    const val Home = "home"
    const val Search = "search"
    const val Profile = "profile"
    const val Watchlist = "watchlist"

    const val MovieDetails = "movie_details/{movieId}"

    fun movieDetailsScreen(movieId: Int): String {
        return "movie_details/$movieId"
    }
}