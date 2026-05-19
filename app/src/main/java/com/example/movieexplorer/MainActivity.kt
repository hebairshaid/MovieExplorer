package com.example.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.movieexplorer.ui.theme.MovieExplorerTheme
import coil.compose.AsyncImage
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieExplorerTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ){
                    composable("login"){        // create a screen
                        LoginScreen(navController)    //
                    }
                    composable("home"){
                        HomeScreen()
                    }
                }

            }
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = email,
            onValueChange = { email = it },

            label = {
                Text("Email")
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = password,
            onValueChange = { password = it },

            label = {
                Text("Password")
            },

            visualTransformation = PasswordVisualTransformation(),

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (errorMessage.isNotEmpty()) {

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {

                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Please fill all fields"
                }

                else if (!email.contains("@")) {
                    errorMessage = "Invalid email format"
                }

                else {
                    errorMessage = "Login Success"
                }
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Login")
        }
    }
}
data class Movie (
    val title : String,
    val rating : String,
    val imageUrl :String
)

@Composable
fun HomeScreen(){
    var selectedTab by remember { mutableStateOf(0) }

    val tabs= listOf("Action","Comedy","Adventure")

    val movie = listOf(
        Movie("batman","8.5","https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg"),
        Movie("superman","7.5","https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg"),
        Movie("spiderman","9","")
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(selectedTabIndex = selectedTab) {  //highlight the selected tab
            tabs.forEachIndexed{        //take index and title then create tab
                index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                    },
                   text = { Text(text = title) }
                )
            }
        }
        when (selectedTab){
            0 -> Text("Action Movie")
            1 -> Text("Comedy Movie")
            2 -> Text("Adventure Movie")
        }
    }

}

@Composable
fun MovieCard(movie : Movie) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ){
     Column (
         modifier = Modifier.padding(16.dp)
     ){
         AsyncImage(
             model = movie.imageUrl,
             contentDescription = movie.title,
             modifier = Modifier.fillMaxSize().height(200.dp)
         )
         Text(text = movie.title)
         Text(text = "Rating ${movie.rating}")
     }
    }
}