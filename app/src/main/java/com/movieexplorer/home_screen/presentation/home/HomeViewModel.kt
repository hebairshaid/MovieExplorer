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
import com.movieexplorer.auth.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val logoutUseCase: LogoutUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel()  {

    private val genreId: Int = savedStateHandle["genreId"] ?: 28 //(28 is default genre fallback)

   /* private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    What this means:
   _state = mutable + internal
   state = read-only for UI
   Compose observes mutableStateOf

   /* This works, but it's Compose-only reactive system*/
    private val _state = MutableStateFlow(HomeState()) //_state writable,only ViewModel can change it
    val state: StateFlow<HomeState> = _state.asStateFlow() //state read-only,UI only observes it
    //.asStateFlow() converts MutableStateFlow → safe read-only StateFlow */

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading) //_state writable,only ViewModel can change it
    val state: StateFlow<HomeUiState> = _state.asStateFlow() //state read-only,UI only observes it
    //.asStateFlow() converts MutableStateFlow → safe read-only StateFlow

    private var currentGenreId: Int = 28

    init {
        loadMovies(currentGenreId)
    }

    fun logout(onLogoutDone: () -> Unit) {
        viewModelScope.launch {
            try {
                logoutUseCase()   // clears session token
                onLogoutDone()    // navigate after clearing
            } catch (e: Exception) {
                println("Logout failed: ${e.message}")
            }
        }
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

           // _state.value = HomeState(isLoading = true)
            _state.value = HomeUiState.Loading

            try {
                val movies: List<Movie> = getMoviesUseCase(genreId)

               /* _state.value = HomeState(
                    movies = movies,
                    isLoading = false
                )*/
                _state.value = HomeUiState.Success(movies)

            } catch (e: Exception) {
               /* _state.value = HomeState(
                    error = e.message,
                    isLoading = false
                )*/
                _state.value = HomeUiState.Error(e.message ?: "Unknown error")
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