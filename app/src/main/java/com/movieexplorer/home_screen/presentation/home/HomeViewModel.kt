package com.movieexplorer.home_screen.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.home_screen.domain.usecase.GetMoviesUseCase
import com.movieexplorer.auth.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val logoutUseCase: LogoutUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // initial genre (Action = 28)
    private val _currentGenre = MutableStateFlow(
        savedStateHandle["genreId"] ?: 28
    )

    val currentGenre: StateFlow<Int> = _currentGenre.asStateFlow()

    /**
     * Paging stream:
     * - listens to genre changes
     * - cancels old paging when genre changes
     * - loads new paging automatically
     * .cachedIn(viewModelScope)
     * Means:
     * “Cache loaded pages inside ViewModel memory”
     * So:
     * rotation ❌ no reload
     * recomposition ❌ no reload
     * navigation back ❌ no reload
     */
    val movies: Flow<PagingData<Movie>> =
        _currentGenre
            .flatMapLatest { genreId ->  //If genre changes → cancel old request → start new one
                getMoviesUseCase(genreId)
            }
           // .cachedIn(viewModelScope)

    /**
     * Handle tab clicks
     */
    fun onTabSelected(index: Int) {
        val genreId = when (index) {
            0 -> 28 // Action
            1 -> 35 // Comedy
            2 -> 12 // Adventure
            else -> 28
        }

        _currentGenre.value = genreId
    }

    /**
     * Logout (kept as-is)
     */
    fun logout(onLogoutDone: () -> Unit) {
        viewModelScope.launch {
            try {
                logoutUseCase()
                onLogoutDone()
            } catch (e: Exception) {
                println("Logout failed: ${e.message}")
            }
        }
    }
}

/*package com.movieexplorer.home_screen.presentation.home

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