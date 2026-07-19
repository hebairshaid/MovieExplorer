package com.movieexplorer.search_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.search_screen.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

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
}
