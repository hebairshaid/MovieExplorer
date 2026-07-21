package com.movieexplorer.profile_screen.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.movieexplorer.ui.components.PasswordTextField
import com.movieexplorer.ui.theme.DarkBlue
import com.movieexplorer.ui.theme.Gold

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPassword by viewModel.currentPassword.collectAsStateWithLifecycle()
    val newPassword by viewModel.newPassword.collectAsStateWithLifecycle()
    val confirmPassword by viewModel.confirmPassword.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()

    var isEditingPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Gold)
        }

        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Gold)
                }
            }

            is ProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = Color.Red)
                }
            }

            is ProfileUiState.Ready -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Profile",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Name", color = Gold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    ProfileReadOnlyField(value = state.name)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Email", color = Gold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    ProfileReadOnlyField(value = state.email)

                    Spacer(modifier = Modifier.height(28.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Change Password",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                isEditingPassword = !isEditingPassword
                                if (!isEditingPassword) {
                                    viewModel.onCurrentPasswordChange("")
                                    viewModel.onNewPasswordChange("")
                                    viewModel.onConfirmPasswordChange("")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit password",
                                tint = Gold
                            )
                        }
                    }

                    if (isEditingPassword) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Current Password", color = Gold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        PasswordTextField(
                            value = currentPassword,
                            onValueChange = viewModel::onCurrentPasswordChange,
                            label = "Enter current password",
                            useOutlinedStyle = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("New Password", color = Gold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        PasswordTextField(
                            value = newPassword,
                            onValueChange = viewModel::onNewPasswordChange,
                            label = "Enter new password",
                            useOutlinedStyle = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Confirm Password", color = Gold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        PasswordTextField(
                            value = confirmPassword,
                            onValueChange = viewModel::onConfirmPasswordChange,
                            label = "Confirm new password",
                            useOutlinedStyle = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = viewModel::savePassword,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Gold,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Save Password", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        message?.let {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = it,
                                color = if (it.contains("success", ignoreCase = true)) {
                                    Gold
                                } else {
                                    Color.Red
                                },
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfileReadOnlyField(value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        enabled = false,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.White,
            disabledBorderColor = Color.Gray,
            disabledContainerColor = Color(0xFF0E1A4A),
            disabledLabelColor = Color.Gray
        )
    )
}
