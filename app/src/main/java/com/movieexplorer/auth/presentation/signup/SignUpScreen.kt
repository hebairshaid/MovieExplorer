package com.movieexplorer.authentication.presentation.signup

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.movieexplorer.auth.presentation.signup.SignUpUiState
import com.movieexplorer.ui.components.PasswordTextField
import com.movieexplorer.ui.theme.Gold

@Composable
fun SignUpScreen(
   // viewModel: SignUpViewModel
    onNavigate: () -> Unit={}
) {

    val viewModel: SignUpViewModel = hiltViewModel()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    //val state = viewModel.state

    val name = viewModel.name.collectAsStateWithLifecycle().value
    val email = viewModel.email.collectAsStateWithLifecycle().value
    val password = viewModel.password.collectAsStateWithLifecycle().value
    val confirmPassword = viewModel.confirmPassword.collectAsStateWithLifecycle().value

    val backDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030B2C))
    ) {

        // 🔙 BACK BUTTON
        IconButton(
            onClick = { backDispatcher?.onBackPressed() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 40.dp, start = 16.dp)
                .size(52.dp)
                .background(Color(0xFF030B2C), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Gold,
                modifier = Modifier.size(32.dp)
            )
        }

        // 🔄 LOADING
       /* if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Gold
            )
        }*/
        when (state) {

            // 🔄 LOADING
            is SignUpUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Gold
                )
            }

            else ->

        // 📄 CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            //  CENTER TITLE
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign Up",
                    color = Gold,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            //  NAME
            Text("Name", color = Gold,fontSize = 20.sp)

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
               // value = state.name,
                value = name,
                onValueChange = viewModel::onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter name") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            //  EMAIL
            Text("Email", color = Gold,fontSize = 20.sp)

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                //value = state.email,
                value = email,
                onValueChange = viewModel::onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter email") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            //  PASSWORD
            Text("Password", color = Gold,fontSize = 20.sp)

            Spacer(modifier = Modifier.height(10.dp))

            PasswordTextField(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = "Enter password"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Confirm Password", color = Gold,fontSize = 20.sp)

            Spacer(modifier = Modifier.height(10.dp))

            PasswordTextField(
                value = confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Confirm Password"
            )

            Spacer(modifier = Modifier.height(30.dp))

            //  CENTER BUTTON
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = viewModel::signUp,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold,
                        contentColor = Color.Black
                    ),

                ) {
                    Text("Sign Up", fontSize = 25.sp)
                }
            }
            // ERROR
            if (state is SignUpUiState.Error) {
                Text(
                    text = state.message,
                    color = Color.Red
                )
            }

            // SUCCESS
            if (state is SignUpUiState.Success) {
                Text(
                    text = state.message,
                    color = Color.Green
                )

                LaunchedEffect(Unit) {
                    onNavigate()
                }
            }

            // ERROR
            /*state.error?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(it, color = Color.Red)
            }

            // SUCCESS
            if (state.success) {
                Spacer(modifier = Modifier.height(10.dp))
                Text("Account created 🎉", color = Color.Green)
            }*/
        }
    }
}
}