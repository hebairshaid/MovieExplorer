package com.movieexplorer.home_screen.presentation.home

import com.movieexplorer.home_screen.domain.model.Movie

sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Success(
        val movies: List<Movie>
    ) : HomeUiState

    data class Error(
        val message: String
    ) : HomeUiState
}

/*
Why sealed interface?

sealed means:

These are the only possible UI states.

No other class can randomly implement HomeUiState outside this file.

That lets the compiler help you. When you handle HomeUiState, Kotlin knows you've covered every possible case.
*/