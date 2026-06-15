package com.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.room.Room

import com.movieexplorer.auth.data.local.SessionManager
import com.movieexplorer.auth.data.repository.SessionRepositoryImpl

import com.movieexplorer.auth.domain.usecase.LoginUseCase
import com.movieexplorer.auth.domain.usecase.LogoutUseCase
import com.movieexplorer.authentication.domain.use_case.CheckSessionUseCase
import com.movieexplorer.authentication.domain.use_case.SignUpUseCase

import com.movieexplorer.authentication.data.local.AppDatabase

import com.movieexplorer.authentication.data.repository.AuthRepositoryImpl

import com.movieexplorer.navigation.NavGraph
import com.movieexplorer.auth.presentation.login.LoginViewModel
import com.movieexplorer.authentication.presentation.signup.SignUpViewModel
import com.movieexplorer.splash_screen.SplashViewModel

import com.movieexplorer.movie_details.data.remote.MovieDetailApiInstance
import com.movieexplorer.movie_details.data.repository.MovieDetailRepositoryImpl
import com.movieexplorer.movie_details.domain.usecase.GetMovieDetailsUseCase
import com.movieexplorer.movie_details.presentation.MovieDetailsViewModelFactory
import com.movieexplorer.home_screen.data.remote.MovieApiInstance
import com.movieexplorer.home_screen.data.repository.MovieRepositoryImpl
import com.movieexplorer.home_screen.domain.usecase.GetMoviesUseCase
import com.movieexplorer.home_screen.presentation.home.HomeViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // NAVIGATION
            val navController = rememberNavController()

            val context = this

            //  ROOM DB
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "movie_db"
            ).build()

            val dao = db.userDao()

            val movieRepository = MovieRepositoryImpl(
                MovieApiInstance.api
            )

            val getMoviesUseCase = GetMoviesUseCase(
                movieRepository
            )

            val homeViewModel = HomeViewModel(
                getMoviesUseCase
            )

            // SESSION
            val sessionManager = SessionManager(context)
            val sessionRepository = SessionRepositoryImpl(sessionManager)

            //  AUTH
            val authRepository = AuthRepositoryImpl(dao)

            //  USE CASES
            val checkSessionUseCase = CheckSessionUseCase(sessionRepository)
            val loginUseCase = LoginUseCase(authRepository, sessionRepository)
            val signUpUseCase = SignUpUseCase(authRepository)

            //  VIEWMODELS
            val splashViewModel = SplashViewModel(checkSessionUseCase)
            val loginViewModel = LoginViewModel(loginUseCase)
            val signUpViewModel = SignUpViewModel(signUpUseCase)

            // 🎬 MOVIE DETAILS FACTORY (ONLY ONCE)
            val movieDetailsFactory = MovieDetailsViewModelFactory(
                GetMovieDetailsUseCase(
                    MovieDetailRepositoryImpl(MovieDetailApiInstance.api)
                )
            )

            val logoutUseCase = LogoutUseCase(sessionRepository)

            //  NAVIGATION
           // val navController = rememberNavController()

            NavGraph(
                navController = navController,
                splashViewModel = splashViewModel,
                loginViewModel = loginViewModel,
                signUpViewModel = signUpViewModel,
                homeViewModel = homeViewModel,
                movieDetailsFactory = movieDetailsFactory
            )
        }
    }
}