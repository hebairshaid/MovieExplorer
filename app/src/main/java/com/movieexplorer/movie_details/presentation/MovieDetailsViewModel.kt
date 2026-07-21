package com.movieexplorer.movie_details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.movie_details.domain.usecase.GetMovieDetailsUseCase
import com.movieexplorer.watchlist_screen.domain.usecase.AddToWatchlistUseCase
import com.movieexplorer.watchlist_screen.domain.usecase.IsInWatchlistUseCase
import com.movieexplorer.watchlist_screen.domain.usecase.RemoveFromWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle,
    private val isInWatchlistUseCase: IsInWatchlistUseCase,
    private val addToWatchlistUseCase: AddToWatchlistUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase
) : ViewModel() {

    private val movieId: Int = savedStateHandle["movieId"] ?: 0

    private val _state = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val state: StateFlow<MovieDetailsUiState> = _state.asStateFlow()

    private val _isInWatchlist = MutableStateFlow(false)
    val isInWatchlist: StateFlow<Boolean> = _isInWatchlist.asStateFlow()

    init {
        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        viewModelScope.launch {
            _state.value = MovieDetailsUiState.Loading
            try {
                val movie = getMovieDetailsUseCase(movieId)
                _state.value = MovieDetailsUiState.Success(movie)
                _isInWatchlist.value = isInWatchlistUseCase(movieId)
            } catch (e: Exception) {
                _state.value = MovieDetailsUiState.Error(
                    e.message ?: "Error loading movie"
                )
            }
        }
    }

    fun toggleWatchlist(movie: Movie) {
        viewModelScope.launch {
            try {
                if (_isInWatchlist.value) {
                    removeFromWatchlistUseCase(movie.id)
                } else {
                    addToWatchlistUseCase(movie)
                }
                _isInWatchlist.value = isInWatchlistUseCase(movie.id)
            } catch (_: Exception) {
                // Keep previous favorite state on failure
            }
        }
    }
}
