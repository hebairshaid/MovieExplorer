package com.movieexplorer.watchlist_screen.data.local

import androidx.room.Entity

@Entity(
    tableName = "watchlist_movies",
    primaryKeys = ["userEmail", "id"]
)
data class WatchlistMovieEntity(
    val id: Int,
    val userEmail: String,
    val title: String,
    val posterUrl: String,
    val rating: Double,
    val releaseDate: String,
    val overview: String,
    val runtime: Int,
    val savedDate: Long
)
