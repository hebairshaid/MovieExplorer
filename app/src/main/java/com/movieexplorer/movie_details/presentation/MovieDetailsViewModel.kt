package com.movieexplorer.movie_details.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.movie_details.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val useCase: GetMovieDetailsUseCase
) : ViewModel() {

    var state by mutableStateOf(MovieDetailsUiState())
        private set

    fun loadMovieDetails(movieId: Int) {

        println("⚡ VIEWMODEL RECEIVED ID = $movieId")

        viewModelScope.launch {

            state = state.copy(isLoading = true)

            try {
                val movie = useCase(movieId)

                println("🎬 MOVIE FROM API = $movie")

                state = state.copy(
                    isLoading = false,
                    movie = movie,
                    error = null
                )

            } catch (e: Exception) {

                println("❌ ERROR = ${e.message}")

                state = state.copy(
                    isLoading = false,
                    movie = null,
                    error = e.message
                )
            }
        }
    }
}