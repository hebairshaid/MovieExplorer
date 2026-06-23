package com.movieexplorer.home_screen.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.navigation.Screen

@Composable
fun MovieItem(
    movie: Movie,
    navController: NavController
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {

                // 🔥 DEBUG LOG
                println("🔥 CLICKED MOVIE ID = ${movie.id}")
                println("🚀 NAVIGATING WITH ID = ${movie.id}")

                // ✅ NAVIGATION FIX
                navController.navigate(Screen.movieDetailsScreen(movie.id))
            },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tap to view details",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}