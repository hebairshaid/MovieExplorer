package com.movieexplorer.home_screen.data.mapper

import com.movieexplorer.home_screen.data.dto.MovieDto
import com.movieexplorer.home_screen.domain.model.Movie

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = poster_path ?: "",
        rating = vote_average
    )
}