package com.movieexplorer.home_screen.domain.model


data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val rating: Double,
    val releaseDate: String,
    val runtime: Int
)