package com.movieexplorer.movie_details.data.remote.dto

data class MovieDetailDto(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val vote_average: Double,
    val release_date: String,
    val runtime: Int
)