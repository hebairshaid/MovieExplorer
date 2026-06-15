package com.movieexplorer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel

import com.movieexplorer.auth.presentation.login.LoginScreen
import com.movieexplorer.auth.presentation.login.LoginViewModel
import com.movieexplorer.authentication.presentation.signup.SignUpScreen
import com.movieexplorer.authentication.presentation.signup.SignUpViewModel
import com.movieexplorer.home_screen.presentation.home.HomeViewModel
import com.movieexplorer.home_screen.presentation.home.MovieScreen

import com.movieexplorer.movie_details.presentation.MovieDetailsScreen
import com.movieexplorer.movie_details.presentation.MovieDetailsViewModel
import com.movieexplorer.movie_details.presentation.MovieDetailsViewModelFactory

import com.movieexplorer.splash_screen.SplashScreenUI
import com.movieexplorer.splash_screen.SplashViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    splashViewModel: SplashViewModel,
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel,
    homeViewModel: HomeViewModel,
    movieDetailsFactory: MovieDetailsViewModelFactory
) {

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreenUI(
                viewModel = splashViewModel,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {

            MovieScreen(
                viewModel = homeViewModel,
                navController = navController,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        } // ✅ IMPORTANT FIX: closing home block

        composable("signup") {
            SignUpScreen(
                viewModel = signUpViewModel
            )
        }

        composable(
            route = "movie_details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->

            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0

            val movieDetailsViewModel: MovieDetailsViewModel =
                viewModel(factory = movieDetailsFactory)

            MovieDetailsScreen(
                movieId = movieId,
                viewModel = movieDetailsViewModel,
                navController = navController
            )
        }
    }
}