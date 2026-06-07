package com.movieexplorer.movie_details.data.mapper
import com.movieexplorer.movie_details.data.remote.dto.MovieDetailDto
import com.movieexplorer.home_screen.domain.model.Movie

fun MovieDetailDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview ?: "",
        posterUrl = poster_path ?: "",
        rating = vote_average,
        releaseDate = release_date ?: ""
    )
}