package com.movieexplorer.movie_details.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.movie_details.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val useCase: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = savedStateHandle["movieId"] ?: 0 //It is a container that holds navigation arguments
    /*
    So when you navigate like this: navController.navigate("movie_details/5")
    Hilt automatically stores: movieId = 5 inside SavedStateHandle
     */
    /*var state by mutableStateOf(MovieDetailsState()) // This is your UI memory it contain loading state,movie data, error message ,state changes → UI recomposes automatically
        private set*/
    private val _state = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val state: StateFlow<MovieDetailsUiState> = _state.asStateFlow()

    init {
        // Runs automatically when the ViewModel is created.
        // No need for the screen to call loadMovieDetails().
        loadMovieDetails()
    }
    /*
    After this change, your MovieDetailsScreen should not call:
    viewModel.loadMovieDetails(...)
    anymore, because the ViewModel loads the movie automatically when it is created.
    with init and private fun
    */

    //fun loadMovieDetails(movieId: Int) { before hilt we need the id
        private fun loadMovieDetails() { //private because only the ViewModel should call it now

        println("⚡ VIEWMODEL RECEIVED ID = $movieId")

        viewModelScope.launch {

            //state = state.copy(isLoading = true)
            _state.value = MovieDetailsUiState.Loading

            try {
                val movie = useCase(movieId)

                println("🎬 MOVIE FROM API = $movie")

                _state.value = MovieDetailsUiState.Success(movie)

                /* state = state.copy(
                     isLoading = false,
                     movie = movie,
                     error = null
                 )*/


            } catch (e: Exception) {

                println("❌ ERROR = ${e.message}")

                _state.value = MovieDetailsUiState.Error(
                    e.message ?: "Error loading movie"
                )

                /*state = state.copy(
                    isLoading = false,
                    movie = null,
                    error = e.message
                )*/
            }
        }
    }
}
/*
This ViewModel is responsible for:
1. Getting movieId from navigation
2. Calling use case to fetch movie details
3. Storing UI state
4. Updating Compose screen

ViewModel is a worker
SavedStateHandle = "ticket with movieId"
UseCase = "job instruction"
state = "notebook for UI"

Worker does:

read ticket → do job → write result → show UI

Why SavedStateHandle it replaces Factory in your case

Because your old factory was doing this:

PASSING runtime values (movieId, genreId)

And SavedStateHandle ALSO provides runtime values.

Important limitation (VERY IMPORTANT)

SavedStateHandle ONLY replaces factories when:

✔ Works for:
navigation arguments
primitive values (Int, String, Boolean)
screen parameters
❌ Does NOT replace factories when:
complex dynamic objects
custom runtime configuration
multiple ViewModel parameters not from navigation

If the value comes from navigation → use SavedStateHandle
If it’s a dependency (repo/usecase) → use Hilt
*/