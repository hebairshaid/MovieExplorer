package com.movieexplorer.splash_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.authentication.domain.use_case.CheckSessionUseCase
import kotlinx.coroutines.launch

class SplashViewModel(
    private val checkSessionUseCase: CheckSessionUseCase
) : ViewModel() {

    var isLoggedIn by mutableStateOf<Boolean?>(null)
        private set

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            checkSessionUseCase.invoke().collect { token ->
                isLoggedIn = !token.isNullOrEmpty()
            }
        }
    }
}