package com.movieexplorer.home_screen.util

fun String?.toPosterUrl(): String { //This function converts a movie poster path into a valid full image URL
   //string? text that can be null,I am adding a custom function to String,This is called an extension function
    return when {
        this.isNullOrBlank() -> ""
        this.startsWith("http") -> this
        else -> "https://image.tmdb.org/t/p/w500$this"
    }
}

//It makes sure every poster image is always a valid, safe URL before displaying it
/*
1. If null/empty → return empty string
2. If already full URL → keep it
3. If only path → convert to full image URL
 */