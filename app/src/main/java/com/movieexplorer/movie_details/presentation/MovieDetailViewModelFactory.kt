/*package com.movieexplorer.movie_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movieexplorer.movie_details.domain.usecase.GetMovieDetailsUseCase

class MovieDetailsViewModelFactory(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            return MovieDetailsViewModel(
                getMovieDetailsUseCase
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/