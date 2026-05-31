package com.movieexplorer.home_screen.data.dto

data class MovieDto(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val vote_average: Double
)