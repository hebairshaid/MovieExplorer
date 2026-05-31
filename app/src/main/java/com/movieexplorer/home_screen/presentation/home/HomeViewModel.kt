package com.movieexplorer.home_screen.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.home_screen.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    fun loadMovies(genreId: Int) {

        viewModelScope.launch {

            _state.value = HomeState(isLoading = true)

            try {
                val movies: List<Movie> = getMoviesUseCase(genreId)

                _state.value = HomeState(
                    movies = movies,
                    isLoading = false
                )

            } catch (e: Exception) {
                _state.value = HomeState(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}