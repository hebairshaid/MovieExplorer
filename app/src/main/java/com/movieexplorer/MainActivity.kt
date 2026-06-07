package com.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.movieexplorer.home_screen.data.remote.MovieApiInstance
import com.movieexplorer.home_screen.data.repository.MovieRepositoryImpl
import com.movieexplorer.home_screen.domain.usecase.GetMoviesUseCase
import com.movieexplorer.home_screen.presentation.home.HomeViewModel
import com.movieexplorer.home_screen.presentation.home.HomeViewModelFactory
import com.movieexplorer.home_screen.presentation.home.MovieScreen
import com.movieexplorer.home_screen.presentation.navigation.NavGraph
import com.movieexplorer.ui.theme.MovieExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            MovieExplorerTheme {

                val navController = rememberNavController()

                NavGraph(navController = navController)
            }
        }
    }

   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            MovieExplorerTheme {

                var showSplash by remember { mutableStateOf(true) }

                // switch after delay
                LaunchedEffect(Unit) {
                    delay(3000)
                    showSplash = false
                }

                if (showSplash) {
                    SplashScreenUI()
                } else {
                    HelloScreen()
                }
            }
        }
    }*/
}

