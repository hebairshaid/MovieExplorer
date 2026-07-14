package com.movieexplorer.home_screen.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.movieexplorer.home_screen.data.mapper.toMovie
import com.movieexplorer.home_screen.data.remote.MovieApi
import com.movieexplorer.home_screen.domain.model.Movie

//It does not display movies or know anything about Compose. It only knows how to fetch pages
//Why don't we pass the page number? Because Paging 3 decides the page number

class MoviePagingSource(
    private val api: MovieApi,
    private val genreId: Int
) : PagingSource<Int, Movie>() {

override suspend fun load( //Retrofit function suspend fun getMovies(...) Since we're calling a suspend function, load() must also be suspend ,This allows Paging to perform network requests without blocking the main UI thread
    params: LoadParams<Int>//params as a message from the Paging library Inside params:
    // key = null Why null? Because this is the first load
): LoadResult<Int, Movie> {

    val page = params.key ?: 1
    println("Genre: $genreId, Page: $page")

    return try {

        val response = api.getMovies(
            genreId = genreId,
            page = page
        )

        println("First movie: ${response.results.firstOrNull()?.title}")
        val movies = response.results.map { it.toMovie() }

        LoadResult.Page(  //Here is one page of data. Also, here is how to load the next/previous page
            data = movies,
            prevKey = null,  //What page should I load before this one?
            nextKey = if (response.page < response.total_pages) {
                response.page + 1
            } else {
                null
            }
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition -> //The last visible item on screen before refresh
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }// prevKey? If we know previous page, go forward one step ,nextKey? If we know next page, go back one step
    }
}
/*
We usually set:

prevKey = null

because:

you start from page 1
you usually don’t scroll backwards in movie apps
TMDb pagination is forward-focused

So we ignore “previous pages

What is getRefreshKey()?

This function is used when:

user rotates screen
app refreshes
Paging restores previous state

It tells Paging:

“If we refresh, what page should we start from?”
If all fails → null

Paging will restart from page 1 automatically

So what is a PagingSource?

Think of it as a bridge between your API and the Paging library.

It has one main responsibility:

"Given a page number, fetch that page of data."
*/