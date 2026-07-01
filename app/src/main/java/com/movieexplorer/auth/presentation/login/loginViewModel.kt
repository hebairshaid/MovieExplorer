package com.movieexplorer.auth.presentation.login

//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieexplorer.auth.domain.usecase.LoginUseCase
//import com.movieexplorer.authentication.presentation.login.LoginState
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel   //Hilt registers this class as a ViewModel and can provide it when requested
class LoginViewModel @Inject constructor( // before You manually called the constructor after Hilt:I know how to create LoginViewModel
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    //var state A variable holding the current screen state state is Everything the screen needs to display
   /* var state by mutableStateOf(LoginState())  //Compose automatically recomposes when state changes
        private set   //The private means only this ViewModel can modify it*/

    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()


    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    /*
    Compose observes StateFlow
    UI updates automatically
    TextField remembers input
    */


    /*fun onEmailChange(value: String) {
        state = state.copy(email = value)  //creates a new LoginState Why copy()  Because LoginState is a data class Only email changes Everything else stays the same
    }*/
    fun onEmailChange(value: String) { //value comes from the UI (Compose TextField)
        _email.value = value
    }

   /* fun onPasswordChange(value: String) {
        state = state.copy(password = value)
    }*/
   fun onPasswordChange(value: String) {
       _password.value = value
   }

    fun login() {

        viewModelScope.launch {

            _state.value = LoginUiState.Loading

            try {
                val user = loginUseCase(
                    _email.value,       //extract value
                    _password.value
                )

                _state.value = LoginUiState.Success(
                    message = "Welcome ${user.name}"
                )

            } catch (e: Exception) {

                _state.value = LoginUiState.Error(
                    message = e.message ?: "Login failed"
                )
            }
        }
    }

    /*fun login() { //Called when Login button is pressed
        viewModelScope.launch {  //loginUseCase is a suspend function Suspend functions must run inside a coroutine
            try {
                state = state.copy(isLoading = true, error = null)

                loginUseCase(state.email, state.password)

                state = state.copy(  //creates new LoginState objects So the screen is constantly getting new immutable state objects
                    isLoading = false,
                    success = true
                )

            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }*/
}
/*
What is a ViewModel?

A ViewModel is the bridge between the UI and the business logic.

Its job is to:

✔ Receive user actions
✔ Call UseCases
✔ Store UI state
✔ Update the screen

Hilt :

Why didn't we touch login(), state, or onEmailChange()?

Because Hilt only changes:

Object creation

It does NOT change:

Business logic
UI state
Coroutines
Compose

Those remain identical.
*/