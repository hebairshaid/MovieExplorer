package com.movieexplorer.watchlist_screen.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query(
        """
        SELECT * FROM watchlist_movies
        WHERE userEmail = :userEmail
        ORDER BY savedDate DESC
        """
    )
    fun observeWatchlist(userEmail: String): Flow<List<WatchlistMovieEntity>>

    @Query(
        """
        SELECT COUNT(*) FROM watchlist_movies
        WHERE userEmail = :userEmail AND id = :movieId
        """
    )
    suspend fun watchlistCount(userEmail: String, movieId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: WatchlistMovieEntity)

    @Query(
        """
        DELETE FROM watchlist_movies
        WHERE userEmail = :userEmail AND id = :movieId
        """
    )
    suspend fun removeMovie(userEmail: String, movieId: Int)
}
