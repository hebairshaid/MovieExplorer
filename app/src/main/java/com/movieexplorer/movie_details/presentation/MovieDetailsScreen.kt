package com.movieexplorer.movie_details.presentation

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel
) {
    println("📺 SCREEN OPENED movieId = $movieId") // STEP 3 DEBUG

    // ✅ IMPORTANT FIX (THIS WAS MISSING BEFORE)
    LaunchedEffect(movieId) {
        println("⚡ CALLING VIEWMODEL movieId = $movieId") // STEP 4 DEBUG
        viewModel.loadMovieDetails(movieId)
    }

    val state = viewModel.state

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        when {

            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.movie != null -> {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = state.movie.title, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = state.movie.overview)
                }
            }

            state.error != null -> {
                Text(text = "Error: ${state.error}")
            }

            else -> {
                Text(text = "No movie found")
            }
        }
    }
}