package com.movieexplorer.search_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.search_screen.domain.usecase.SearchMoviesUseCase
import com.movieexplorer.watchlist_screen.domain.usecase.AddToWatchlistUseCase
import com.movieexplorer.watchlist_screen.domain.usecase.ObserveWatchlistUseCase
import com.movieexplorer.watchlist_screen.domain.usecase.RemoveFromWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    observeWatchlistUseCase: ObserveWatchlistUseCase,
    private val addToWatchlistUseCase: AddToWatchlistUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    val favoriteIds: StateFlow<Set<Int>> = observeWatchlistUseCase()
        .map { movies -> movies.map { it.id }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    init {
        _query
            .debounce(400) // wait ~400ms after user stops typing
            .distinctUntilChanged() // skip if text didn’t change
            .onEach { text ->
                if (text.isBlank()) {
                    _uiState.value = SearchUiState.Idle
                }
            }
            .filter { it.isNotBlank() }
            .flatMapLatest { searchTerm ->
                flow {
                    emit(SearchUiState.Loading)
                    try {
                        val movies = searchMoviesUseCase(searchTerm)
                        if (movies.isEmpty()) {
                            emit(SearchUiState.Empty(searchTerm))
                        } else {
                            emit(SearchUiState.Success(movies))
                        }
                    } catch (e: Exception) {
                        emit(SearchUiState.Error(e.message ?: "Search failed"))
                    }
                }
            }
            .onEach { state -> _uiState.value = state }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun clearQuery() {
        _query.value = ""
        _uiState.value = SearchUiState.Idle
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
}
