package com.movieexplorer.home_screen.data.mapper

import com.movieexplorer.home_screen.data.local.CachedMovieEntity
import com.movieexplorer.home_screen.domain.model.Movie

fun Movie.toEntity(genreId: Int): CachedMovieEntity {
    return CachedMovieEntity(
        id = id,
        title = title,
        posterUrl = posterUrl,
        rating = rating,
        releaseDate = releaseDate,
        overview = overview,
        runtime = runtime,
        genreId = genreId
    )
}

fun CachedMovieEntity.toMovie(): Movie {
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