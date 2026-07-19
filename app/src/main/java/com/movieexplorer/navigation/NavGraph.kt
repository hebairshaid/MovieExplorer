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

import com.movieexplorer.search_screen.presentation.SearchScreen

import com.movieexplorer.splash_screen.SplashScreenUI
import com.movieexplorer.splash_screen.SplashViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NavGraph( //This is a function that defines all screens in your app and how to navigate between them
navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // 🔵 SPLASH
        composable(Screen.Splash) {
            SplashScreenUI(
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

        // 🔵 LOGIN
        composable(Screen.Login) {
            LoginScreen(
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

        // 🔵 HOME
        composable(Screen.Home) {
            MovieScreen(
                navController = navController,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // 🔵 SEARCH
        composable(Screen.Search) {
            SearchScreen(navController = navController)
        }

        // 🔵 SIGNUP
        composable(Screen.SignUp) {
            SignUpScreen(
                onNavigate = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        // 🔵 MOVIE DETAILS
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

   /* NavHost( // container for all screens
        navController = navController,
        startDestination = "splash" //first screen when app open
    ) {

        composable("splash") {
            SplashScreenUI(
                viewModel = splashViewModel,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true } //Removes splash from back stack So user cannot go back to it
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
        } // IMPORTANT FIX: closing home block

        composable("signup") {
            SignUpScreen(
                viewModel = signUpViewModel
            )
        }

        composable(
            route = "movie_details/{movieId}", //This is a dynamic route it accept an id
            arguments = listOf(navArgument("movieId") { type = NavType.IntType }) //movie id must be int
        ) { backStackEntry ->

            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0 //If no value → default = 0

            val movieDetailsViewModel: MovieDetailsViewModel =  //This creates ViewModel using factory
                viewModel(factory = movieDetailsFactory)        //this when a view model need a parameter

            MovieDetailsScreen(
                movieId = movieId,
                viewModel = movieDetailsViewModel,
                navController = navController
            )
        }
    }
}
*/
