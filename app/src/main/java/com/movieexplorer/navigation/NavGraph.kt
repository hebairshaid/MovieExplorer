package com.movieexplorer.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.movieexplorer.auth.presentation.login.LoginScreen
import com.movieexplorer.authentication.presentation.signup.SignUpScreen
import com.movieexplorer.home_screen.presentation.home.MovieScreen
import com.movieexplorer.movie_details.presentation.MovieDetailsScreen
import com.movieexplorer.profile_screen.presentation.ProfileScreen
import com.movieexplorer.search_screen.presentation.SearchScreen
import com.movieexplorer.splash_screen.SplashScreenUI
import com.movieexplorer.ui.theme.DarkBlue
import com.movieexplorer.watchlist_screen.presentation.WatchlistScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Splash,
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue)
        ) {

            composable(Screen.Splash) {
                SplashScreenUI(
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.Login) {
                LoginScreen(
                    onNavigateToSignUp = {
                        navController.navigate(Screen.SignUp)
                    },
                    onLoginSuccess = {
                        navController.navigate(Screen.Home) {
                            // Clear Splash/Login so phone back cannot return to login
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.Home) {
                MovieScreen(
                    navController = navController,
                    onLogout = {
                        navController.navigate(Screen.Login) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.Search) {
                SearchScreen(navController = navController)
            }

            composable(Screen.Profile) {
                ProfileScreen(navController = navController)
            }

            composable(Screen.Watchlist) {
                WatchlistScreen(navController = navController)
            }

            composable(Screen.SignUp) {
                SignUpScreen(
                    onNavigate = {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.SignUp) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route = Screen.MovieDetails,
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) {
                MovieDetailsScreen(
                    navController = navController,
                    movieId = it.arguments?.getInt("movieId") ?: 0
                )
            }
        }
    }
}
