package com.movieexplorer.movie_details.presentation

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.movieexplorer.home_screen.presentation.home.FavoriteHeartButton
import com.movieexplorer.home_screen.util.toPosterUrl
import com.movieexplorer.ui.theme.Gold

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    //viewModel: MovieDetailsViewModel,
    navController: NavHostController
) {
    val viewModel: MovieDetailsViewModel = hiltViewModel()
    //val viewModel: MovieDetailsViewModel = viewModel()

    /*LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }*/

   // val state = viewModel.state
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val isInWatchlist = viewModel.isInWatchlist.collectAsStateWithLifecycle().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030B2C))
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {

      /*  when {

            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Gold
                )
            }

            state.movie != null -> */
        when (state) {

            is MovieDetailsUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Gold
                )
            }

            is MovieDetailsUiState.Success ->{

                val movie = state.movie

                val genre =
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<String>("genre") ?: "Unknown"

                val year =
                    movie.releaseDate.take(4)

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                    // Poster Image
                    AsyncImage(
                        model = movie.posterUrl.toPosterUrl(),
                        contentDescription = movie.title,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    )

                    // Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0x66000000),
                                        Color(0xFF030B2C),
                                        Color(0xFF030B2C)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        // Back Button
                        val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp, start = 20.dp, end = 20.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    backDispatcher?.onBackPressed()
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .size(52.dp)
                                    .background(
                                        Color(0x55000000),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }

                            FavoriteHeartButton(
                                isFavorite = isInWatchlist,
                                onClick = { viewModel.toggleWatchlist(movie) },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(220.dp)
                        )

                        Column(
                            modifier = Modifier.padding(
                                horizontal = 24.dp
                            )
                        ) {

                            // Movie Title
                            Text(
                                text = movie.title,
                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 30.sp
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            // Rating Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "⭐",
                                    fontSize = 18.sp
                                )

                                Spacer(
                                    modifier = Modifier.width(6.dp)
                                )

                                Text(
                                    text = String.format(
                                        "%.1f",
                                        movie.rating
                                    ),
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "/10",
                                    color = Color.Gray,
                                    fontSize = 15.sp
                                )

                                Text(
                                    text = "  •  ",
                                    color = Color.Gray
                                )

                                Text(
                                    text = year,
                                    color = Color.Gray,
                                    fontSize = 15.sp
                                )

                                Text(
                                    text = "  •  ",
                                    color = Color.Gray
                                )

                                Text(
                                    text = "${movie.runtime} min",
                                    color = Color.Gray,
                                    fontSize = 15.sp
                                )
                            }

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            // Genre Chip
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = genre,
                                        color = Gold,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Color(0xFF18284A)
                                ),
                                border = null,
                                shape = RoundedCornerShape(50)
                            )

                            Spacer(
                                modifier = Modifier.height(25.dp)
                            )

                            // Overview Title
                            Text(
                                text = "Overview",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            // Overview Text
                            Text(
                                text = movie.overview,
                                color = Color(0xFF9AA4C3),
                                fontSize = 16.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
            is MovieDetailsUiState.Error -> {
                Text(
                    text = state.message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
           /* state.error != null -> {
                Text(
                    text = "Error: ${state.error}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }*/

            else -> {
                Text(
                    text = "No movie found",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}