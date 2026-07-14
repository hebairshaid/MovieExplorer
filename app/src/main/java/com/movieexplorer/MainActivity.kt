package com.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.movieexplorer.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //it tells hilt This Android class can receive dependencies from Hilt
class MainActivity : ComponentActivity() { //create everything and connect everything together this run first

    override fun onCreate(savedInstanceState: Bundle?) { //Called when app starts. Android puts:movieId=5 inside a Bundle internally.
        super.onCreate(savedInstanceState)
        //deleteDatabase("movie_db")
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent { //Everything inside here is UI + setup

            val navController = rememberNavController() //This controls screen navigation

            NavGraph(
                navController = navController
            )
        }
    }
}
//A Bundle is a key-value container used to pass data between
// Android components like Activities and Screens.

/*package com.movieexplorer

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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint  //it tells hilt This Android class can receive dependencies from Hilt
class MainActivity : ComponentActivity() { //create everything and connect everything together this run first

    override fun onCreate(savedInstanceState: Bundle?) { //Called when app starts.
        super.onCreate(savedInstanceState)

        setContent { //Everything inside here is UI + setup

            // NAVIGATION
            val navController = rememberNavController() //This controls screen navigation

            val context = this //Gives access to Android system (needed for DB, DataStore, etc)

            //  ROOM DB
            val db = Room.databaseBuilder( //Creates database named "movie_db"
                context,                   //uses appDatabase
                AppDatabase::class.java,
                "movie_db"
            ).build()

            val dao = db.userDao() //Gets access to database functions insert check login

            val movieRepository = MovieRepositoryImpl( //Connects to internet (API)
                MovieApiInstance.api
            )

            val getMoviesUseCase = GetMoviesUseCase( //fetch movies
                movieRepository
            )

            val homeViewModel = HomeViewModel(  //UI controller for Home screen
                getMoviesUseCase
            )

            // SESSION
            val sessionManager = SessionManager(context)  //Saves token in DataStore
            val sessionRepository = SessionRepositoryImpl(sessionManager)

            //  AUTH
            val authRepository = AuthRepositoryImpl(dao) //Talks to ROOM database for:login and signup

            //  USE CASES
            val checkSessionUseCase = CheckSessionUseCase(sessionRepository)//Used in splash screen
            val loginUseCase = LoginUseCase(authRepository, sessionRepository)
            val signUpUseCase = SignUpUseCase(authRepository)

            //  VIEWMODELS
            val splashViewModel = SplashViewModel(checkSessionUseCase) //decide go to login OR home
            val loginViewModel = LoginViewModel(loginUseCase)
            val signUpViewModel = SignUpViewModel(signUpUseCase)

            // MOVIE DETAILS FACTORY (ONLY ONCE)
            val movieDetailsFactory = MovieDetailsViewModelFactory(
                GetMovieDetailsUseCase(
                    MovieDetailRepositoryImpl(MovieDetailApiInstance.api)
                )
            )

           // val logoutUseCase = LogoutUseCase(sessionRepository)

            //  NAVIGATION
           // val navController = rememberNavController()

            NavGraph( //Sends everything into navigation system
                navController = navController,
                splashViewModel = splashViewModel,
                loginViewModel = loginViewModel,
                signUpViewModel = signUpViewModel,
                homeViewModel = homeViewModel,
                movieDetailsFactory = movieDetailsFactory
            )
        }
    }
}*/