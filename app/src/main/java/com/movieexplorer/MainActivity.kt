package com.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.movieexplorer.authentication.data.local.AppDatabase
import com.movieexplorer.authentication.data.repository.AuthRepositoryImpl
import com.movieexplorer.authentication.domain.use_case.SignUpUseCase
import com.movieexplorer.authentication.presentation.signup.SignUpScreen
import com.movieexplorer.authentication.presentation.signup.SignUpViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MovieApp()
        }
    }
}

@Composable
fun MovieApp() {

    val context = LocalContext.current

    // Room database
    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "movie_db"
    ).build()

    val dao = db.userDao()

    val repository = AuthRepositoryImpl(dao)
    val useCase = SignUpUseCase(repository)

    val viewModel = SignUpViewModel(useCase)

    MaterialTheme {
        Surface {
            SignUpScreen(viewModel = viewModel)
        }
    }
}