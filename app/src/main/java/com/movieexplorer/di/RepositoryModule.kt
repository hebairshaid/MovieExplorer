package com.movieexplorer.di

import com.movieexplorer.home_screen.data.local.MovieDao
import com.movieexplorer.home_screen.data.local.MovieDatabase
import com.movieexplorer.home_screen.data.remote.MovieApi
import com.movieexplorer.home_screen.data.repository.MovieRepositoryImpl
import com.movieexplorer.home_screen.domain.repository.MovieRepository
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
}