package com.movieexplorer.home_screen.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.movieexplorer.home_screen.domain.model.Movie

@Composable
fun MovieCard(movie: Movie) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Image(
                painter = rememberAsyncImagePainter(
                    "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = movie.title)
            Text(text = "Rating: ${movie.rating}")
        }
    }
}