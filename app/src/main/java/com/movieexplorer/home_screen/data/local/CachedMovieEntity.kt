package com.movieexplorer.home_screen.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_movies") //Without specifying tableName, Room would use the class name (CachedMovieEntity) as the table name
data class CachedMovieEntity(

    @PrimaryKey
    val id: Int,

    val title: String,
    val posterUrl: String,
    val rating: Double,
    val releaseDate: String,
    val overview: String,
    val runtime: Int,
    // Used to know which tab (genre) this movie belongs to
    val genreId: Int
)