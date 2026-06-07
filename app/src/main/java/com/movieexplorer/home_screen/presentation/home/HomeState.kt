package com.movieexplorer.home_screen.presentation.home


import com.movieexplorer.home_screen.domain.model.Movie

data class HomeState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)