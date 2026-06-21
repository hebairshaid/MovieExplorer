package com.movieexplorer.home_screen.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.home_screen.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.SavedStateHandle

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val genreId: Int = savedStateHandle["genreId"] ?: 28 //(28 is default genre fallback)

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state


    private var currentGenreId: Int = 28

    init {
        loadMovies(currentGenreId)
    }

    fun onTabSelected(index: Int) {
        currentGenreId = when (index) {
            0 -> 28
            1 -> 35
            2 -> 12
            else -> 28
        }

        loadMovies(currentGenreId)
    }

    //fun loadMovies(genreId: Int) {
    private fun loadMovies(genreId: Int){

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
/*
Now your HomeScreen MUST NOT call:

viewModel.loadMovies()

or:

viewModel.loadMovies(genreId)

because ViewModel does it automatically by the init
the save state handle for generid
*/