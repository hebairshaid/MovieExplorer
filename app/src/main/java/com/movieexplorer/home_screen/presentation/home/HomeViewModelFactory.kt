package com.movieexplorer.home_screen.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movieexplorer.home_screen.domain.usecase.GetMoviesUseCase

class HomeViewModelFactory(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(getMoviesUseCase) as T
    }
}