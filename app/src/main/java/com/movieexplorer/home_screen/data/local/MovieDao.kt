package com.movieexplorer.home_screen.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao //tell room Generate the implementation of this interface generates it automatically during compilation
interface MovieDao { //Why is it an interface? A DAO only describes what operations are possible Room generates the actual implementation

    @Insert(onConflict = OnConflictStrategy.REPLACE) //Why REPLACE (IGNORE,ABORT,FAIL,REPLACE) We choose REPLACE because we always want the newest movie information from TMDb
    suspend fun insertMovies( ////suspend By marking the function as suspend, Room executes it asynchronously when called from a coroutine
        movies: List<CachedMovieEntity>
    )//Without a conflict strategy, Room wouldn't know what to do because the primary key already exists

    @Query("SELECT * FROM cached_movies WHERE genreId = :genreId") //:genreID The : tells Room Replace this with the function parameter
     fun getMoviesByGenre(
        genreId: Int
    ): PagingSource<Int, CachedMovieEntity>

    @Query("DELETE FROM cached_movies WHERE genreId = :genreId")
    suspend fun clearGenre(
        genreId: Int
    )
    @Query("SELECT * FROM cached_movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): CachedMovieEntity?

    @Query("DELETE FROM cached_movies")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM cached_movies")
    suspend fun countMovies(): Int
}

/*If you simply insert the new list, old movies that no longer belong in the cache could remain.
Instead we:

Delete the old cache for that genre.
Insert the fresh movies.

This keeps the cache synchronized with the latest API response.*/