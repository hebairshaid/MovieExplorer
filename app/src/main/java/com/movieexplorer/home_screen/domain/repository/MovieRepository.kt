package com.movieexplorer.home_screen.domain.repository

import com.movieexplorer.home_screen.domain.model.Movie
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(genreId: Int): Flow<PagingData<Movie>>
}
/*
Flow<PagingData<Movie>>

This means:

stream of pages
not a single response
supports infinite scrolling
Flow not suspend continuous stream
 */

/*interface MovieRepository {
    suspend fun getMovies(genreId: Int): List<Movie>
}*/