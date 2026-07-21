package com.movieexplorer.watchlist_screen.data.mapper

import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.watchlist_screen.data.local.WatchlistMovieEntity

fun Movie.toWatchlistEntity(userEmail: String): WatchlistMovieEntity {
    return WatchlistMovieEntity(
        id = id,
        userEmail = userEmail,
        title = title,
        posterUrl = posterUrl,
        rating = rating,
        releaseDate = releaseDate,
        overview = overview,
        runtime = runtime,
        savedDate = System.currentTimeMillis()
    )
}

fun WatchlistMovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        posterUrl = posterUrl,
        rating = rating,
        releaseDate = releaseDate,
        overview = overview,
        runtime = runtime
    )
}
