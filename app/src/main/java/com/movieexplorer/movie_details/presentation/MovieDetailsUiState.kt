package com.movieexplorer.movie_details.presentation

import com.movieexplorer.home_screen.domain.model.Movie

sealed interface MovieDetailsUiState {

    data object Loading : MovieDetailsUiState

    data class Success(val movie: Movie) : MovieDetailsUiState

    data class Error(val message: String) : MovieDetailsUiState
}