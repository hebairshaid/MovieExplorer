package com.movieexplorer.di

import com.movieexplorer.movie_details.data.repository.MovieDetailRepositoryImpl
import com.movieexplorer.movie_details.domain.repository.MovieDetailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieDetailModule {

    @Provides
    @Singleton
    fun provideMovieDetailRepository(
        api: com.movieexplorer.movie_details.data.remote.MovieDetailApi
    ): MovieDetailRepository {
        return MovieDetailRepositoryImpl(api)
    }
}