package com.movieexplorer.home_screen.data.dto

data class MovieResponseDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,    //should we stop loading more data?
    val total_results: Int
)