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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieexplorer.ui.theme.DarkBlue
import com.movieexplorer.ui.theme.Gold

@Composable
fun MovieScreen(viewModel: HomeViewModel) {

    var selectedTab by remember { mutableStateOf(0) }

    val state = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.loadMovies(28)
    }

    val tabs = listOf("Action", "Comedy", "Adventure")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "🎬",
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Movie Explorer",
                color = Color.White,
                fontSize = 28.sp

            )
        }

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

                        when (index) {
                            0 -> viewModel.loadMovies(28)
                            1 -> viewModel.loadMovies(35)
                            2 -> viewModel.loadMovies(12)
                        }
                    },
                    text = {
                        Text(title)
                    }
                )
            }
        }

        when {

            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Gold)
                }
            }

            state.error != null -> {
                Text(
                    text = state.error ?: "",
                    color = Color.White
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.movies) { movie ->
                        MovieCard(
                            movie = movie,
                            genre = tabs[selectedTab]
                        )
                    }
                }
            }
        }
    }
}



