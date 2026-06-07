package com.movieexplorer.home_screen.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType

import com.movieexplorer.home_screen.data.remote.MovieApiInstance
import com.movieexplorer.home_screen.data.repository.MovieRepositoryImpl
import com.movieexplorer.home_screen.domain.usecase.GetMoviesUseCase
import com.movieexplorer.home_screen.presentation.home.HomeViewModel
import com.movieexplorer.home_screen.presentation.home.MovieScreen

import com.movieexplorer.movie_details.data.remote.MovieDetailApiInstance
import com.movieexplorer.movie_details.data.repository.MovieDetailRepositoryImpl
import com.movieexplorer.movie_details.domain.usecase.GetMovieDetailsUseCase
import com.movieexplorer.movie_details.presentation.MovieDetailsScreen
import com.movieexplorer.movie_details.presentation.MovieDetailsViewModel
import com.movieexplorer.movie_details.presentation.MovieDetailsViewModelFactory

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {

            val api = MovieApiInstance.api
            val repository = MovieRepositoryImpl(api)
            val useCase = GetMoviesUseCase(repository)

            val homeViewModel = HomeViewModel(useCase)

            MovieScreen(
                viewModel = homeViewModel,
                navController = navController
            )
        }

        composable(
            route = "movie_details/{movieId}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0

            val viewModel: MovieDetailsViewModel = viewModel(
                factory = MovieDetailsViewModelFactory(
                    GetMovieDetailsUseCase(
                        MovieDetailRepositoryImpl(MovieDetailApiInstance.api)
                    )
                )
            )

            MovieDetailsScreen(
                movieId = movieId,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}