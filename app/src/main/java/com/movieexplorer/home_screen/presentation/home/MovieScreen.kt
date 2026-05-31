package com.movieexplorer.home_screen.presentation.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.movieexplorer.home_screen.domain.model.Movie

@Composable
fun MovieScreen(viewModel: HomeViewModel) {

    val state = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.loadMovies(28)
    }

    when {
        state.isLoading -> {
            Text("Loading...")
        }

        state.error != null -> {
            Text("Error: ${state.error}")
        }

        else -> {
            LazyColumn {
                items(state.movies) { movie ->
                    MovieCard(movie)
                }
            }
        }
    }
}