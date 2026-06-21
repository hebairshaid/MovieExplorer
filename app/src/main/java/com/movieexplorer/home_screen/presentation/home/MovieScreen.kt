package com.movieexplorer.home_screen.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*
import com.movieexplorer.R
import com.movieexplorer.ui.theme.DarkBlue
import com.movieexplorer.ui.theme.Gold

@Composable
fun MovieScreen(
    //viewModel: HomeViewModel,
    navController: NavController,
    onLogout: () -> Unit
) {

    val viewModel: HomeViewModel = hiltViewModel()
    var selectedTab by remember { mutableStateOf(0) }

    val state = viewModel.state.value

    /*LaunchedEffect(Unit) {
        viewModel.loadMovies(28)
    }*/

    val tabs = listOf("Action", "Comedy", "Adventure")

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition
    )

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = Gold.toArgb(),
            keyPath = arrayOf("**", "Fill 1")
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value = Gold.toArgb(),
            keyPath = arrayOf("**", "Stroke 1")
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            LottieAnimation(
                composition = composition,
                progress = progress,
                dynamicProperties = dynamicProperties,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Movie Explorer",
                color = Color.White,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor = Color.Black
                )
            ) {
                Text("Logout")
            }
        }

        // TABS
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkBlue,
            contentColor = Gold,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(3.dp)
                        .padding(horizontal = 20.dp)
                        .background(
                            color = Gold,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        ) {

            tabs.forEachIndexed { index, title ->

                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        viewModel.onTabSelected(index)
                    },
                    /*onClick = {
                        selectedTab = index

                        when (index) {
                            0 -> viewModel.loadMovies(28)
                            1 -> viewModel.loadMovies(35)
                            2 -> viewModel.loadMovies(12)
                        }
                    },*/
                    text = {
                        Text(title)
                    }
                )
            }
        }

        // CONTENT
        when {

            state.isLoading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Gold
                    )
                }
            }

            state.error != null -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "",
                        color = Color.White
                    )
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(state.movies) { movie ->

                        MovieCard(
                            movie = movie,
                            genre = tabs[selectedTab],
                            onClick = {

                                println("🔥 CLICKED MOVIE ID = ${movie.id}")

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("genre", tabs[selectedTab])

                                navController.navigate(
                                    "movie_details/${movie.id}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}