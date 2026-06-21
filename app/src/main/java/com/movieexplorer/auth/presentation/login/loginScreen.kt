package com.movieexplorer.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*
import com.movieexplorer.R
import com.movieexplorer.ui.theme.Gold
import androidx.hilt.navigation.compose.hiltViewModel

@Composable //This function draws UI
fun LoginScreen( //This screen is where everything comes together
    //viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit, // Give me a function to run when login succeeds. I don't need any parameters or return value
    onNavigateToSignUp: () -> Unit //Give me a function. When I call it, it will navigate to Sign Up
) {

    val viewModel: LoginViewModel = hiltViewModel()
    val state = viewModel.state

    val composition by rememberLottieComposition( //loads
        LottieCompositionSpec.RawRes(R.raw.splash)
    )
    val progress by animateLottieCompositionAsState(composition) //Animates it

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = Gold.toArgb(),
            keyPath = arrayOf("**", "Fill 1")
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value = Gold.toArgb(),
            keyPath = arrayOf("**", "Stroke 1")
        )
    )



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030B2C))
    ) {

        //  LOADING
        if (state.isLoading) { //come from viewModel
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Gold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Box (
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    dynamicProperties = dynamicProperties,
                    modifier = Modifier.size(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                //  TITLE
                Text(
                    text = "Login",
                    color = Gold,
                    style = MaterialTheme.typography.headlineLarge
                )
            }



            Spacer(modifier = Modifier.height(40.dp))

            // EMAIL
            Text("Email", color = Gold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = state.email, //Shows current email.
                onValueChange = viewModel::onEmailChange,// is shorthand for onValueChange = { viewModel.onEmailChange(it)}
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter email") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // PASSWORD
            Text("Password", color = Gold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter password") }
            )

            Spacer(modifier = Modifier.height(30.dp))

            //  LOGIN BUTTON
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = viewModel::login,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold,
                        contentColor = Color.Black
                    ),

                    ) {
                    Text("login", fontSize = 25.sp)
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
            TextButton(
                    onClick = onNavigateToSignUp
                ) {
                    Text(
                        text = "Signup",
                        color = Gold,
                        fontSize = 10.sp
                    )
                }
            }
            //  ERROR
            state.error?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(it, color = Color.Red)
            }

            // SUCCESS
            if (state.success) {
                LaunchedEffect(Unit) {
                    onLoginSuccess() //runs Usually navController.navigate("home")
                }
            }
        }
    }
}