package com.movieexplorer.movie_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movieexplorer.movie_details.domain.usecase.GetMovieDetailsUseCase

class MovieDetailsViewModelFactory(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(getMovieDetailsUseCase) as T
    }
}