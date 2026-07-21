package com.movieexplorer.home_screen.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.home_screen.domain.usecase.GetMoviesUseCase
import com.movieexplorer.auth.domain.usecase.LogoutUseCase
import com.movieexplorer.home_screen.util.NetworkMonitor
import com.movieexplorer.watchlist_screen.domain.usecase.AddToWatchlistUseCase
import com.movieexplorer.watchlist_screen.domain.usecase.ObserveWatchlistUseCase
import com.movieexplorer.watchlist_screen.domain.usecase.RemoveFromWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val logoutUseCase: LogoutUseCase,
    observeWatchlistUseCase: ObserveWatchlistUseCase,
    private val addToWatchlistUseCase: AddToWatchlistUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase,
    networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _currentGenre = MutableStateFlow(
        savedStateHandle["genreId"] ?: 28
    )

    val currentGenre: StateFlow<Int> = _currentGenre.asStateFlow()

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    val movies: Flow<PagingData<Movie>> =
        _currentGenre
            .flatMapLatest { genreId ->
                getMoviesUseCase(genreId)
            }
            .cachedIn(viewModelScope)

    val favoriteIds: StateFlow<Set<Int>> = observeWatchlistUseCase()
        .map { movies -> movies.map { it.id }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    fun onTabSelected(index: Int) {
        val genreId = when (index) {
            0 -> 28 // Action
            1 -> 35 // Comedy
            2 -> 12 // Adventure
            else -> 28
        }

        _currentGenre.value = genreId
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            try {
                if (favoriteIds.value.contains(movie.id)) {
                    removeFromWatchlistUseCase(movie.id)
                } else {
                    addToWatchlistUseCase(movie)
                }
            } catch (e: Exception) {
                println("Favorite toggle failed: ${e.message}")
            }
        }
    }

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
