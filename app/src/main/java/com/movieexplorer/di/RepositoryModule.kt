package com.movieexplorer.di

import com.movieexplorer.auth.domain.session.SessionRepository
import com.movieexplorer.home_screen.data.local.MovieDao
import com.movieexplorer.home_screen.data.local.MovieDatabase
import com.movieexplorer.home_screen.data.remote.MovieApi
import com.movieexplorer.home_screen.data.repository.MovieRepositoryImpl
import com.movieexplorer.home_screen.domain.repository.MovieRepository
import com.movieexplorer.search_screen.data.repository.SearchRepositoryImpl
import com.movieexplorer.search_screen.domain.repository.SearchRepository
import com.movieexplorer.watchlist_screen.data.local.WatchlistDao
import com.movieexplorer.watchlist_screen.data.repository.WatchlistRepositoryImpl
import com.movieexplorer.watchlist_screen.domain.repository.WatchlistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: MovieApi,
        dao: MovieDao,
        database: MovieDatabase
    ): MovieRepository {
        return MovieRepositoryImpl(api, dao, database)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        api: MovieApi
    ): SearchRepository {
        return SearchRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideWatchlistRepository(
        dao: WatchlistDao,
        sessionRepository: SessionRepository
    ): WatchlistRepository {
        return WatchlistRepositoryImpl(dao, sessionRepository)
    }
}
