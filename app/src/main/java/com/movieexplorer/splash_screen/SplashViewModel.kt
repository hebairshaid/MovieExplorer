package com.movieexplorer.splash_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.authentication.domain.use_case.CheckSessionUseCase
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkSessionUseCase: CheckSessionUseCase
) : ViewModel() {

    var isLoggedIn by mutableStateOf<Boolean?>(null) //Boolean? mean true false null ,null mean I don't know yet because the session check hasn't finished
        private set

    init { //What is init? init runs automatically when the object is created Immediately constructor runs init runs
        checkSession() //runs automatically when SplashViewModel is created
    }

    private fun checkSession() {  //why private? Only SplashViewModel should use it
        viewModelScope.launch {  //why? Because checkSessionUseCase() returns a Flow ,Flows are collected inside coroutines
            checkSessionUseCase.invoke().collect { token -> // .collect{token -> mean Listen for values from the Flow
                isLoggedIn = !token.isNullOrEmpty() //return true token is missing return false token exists
            }
        }
    }
}