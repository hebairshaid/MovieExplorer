package com.movieexplorer.authentication.domain.use_case

import com.movieexplorer.authentication.domain.model.User
import com.movieexplorer.authentication.domain.repository.AuthRepository

class SignUpUseCase(  //to save new user
    private val repository: AuthRepository
) {

    suspend operator fun invoke(user: User) { //You call this like a function signUpUseCase(user)
    //This use case is like a security guard before saving data
        if (user.name.isBlank()) {       //name cannot be empty
            throw Exception("Name is required")
        }

        //  EMAIL VALIDATION (HERE)
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()  //email must look like: test@gmail.com

        if (!emailRegex.matches(user.email)) {
            throw Exception("Invalid email format")
        }

        // optional duplicate check
        if (repository.isEmailExists(user.email)) {
            throw Exception("Email already exists")
        }

        // password check
        if (user.password.length < 6) {
            throw Exception("Password must be at least 6 characters")
        }

        if (!user.password.any { it.isUpperCase() }) { // at least one
            throw Exception("Password must contain a capital letter")
        }

        if (!user.password.any { !it.isLetterOrDigit() }) {
            throw Exception("Password must contain a special character")
        }

        repository.signUp(user)
    }
}