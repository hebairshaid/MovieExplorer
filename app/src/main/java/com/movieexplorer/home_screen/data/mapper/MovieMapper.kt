package com.movieexplorer.home_screen.data.mapper

import com.movieexplorer.home_screen.data.dto.MovieDto
import com.movieexplorer.home_screen.domain.model.Movie

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title ?: "",
        posterUrl = "https://image.tmdb.org/t/p/w500${poster_path ?: ""}",
        rating = vote_average ?: 0.0,
        releaseDate = release_date ?: "",
        overview = overview ?: ""
    )
}