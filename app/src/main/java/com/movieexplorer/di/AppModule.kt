package com.movieexplorer.di

import android.content.Context
import androidx.room.Room
import com.movieexplorer.auth.data.local.AppDatabase
import com.movieexplorer.auth.data.local.UserDao
import com.movieexplorer.auth.data.repository.AuthRepositoryImpl
import com.movieexplorer.auth.data.repository.SessionRepositoryImpl
import com.movieexplorer.auth.data.security.BCryptPasswordHasher
import com.movieexplorer.auth.data.security.SecureSessionManager
import com.movieexplorer.auth.domain.repository.AuthRepository
import com.movieexplorer.auth.domain.security.PasswordHasher
import com.movieexplorer.auth.domain.session.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "movie_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideSecureSessionManager(
        @ApplicationContext context: Context
    ): SecureSessionManager = SecureSessionManager(context)

    @Provides
    fun provideSessionRepository(
        sessionManager: SecureSessionManager
    ): SessionRepository = SessionRepositoryImpl(sessionManager)

    @Provides
    @Singleton
    fun providePasswordHasher(): PasswordHasher = BCryptPasswordHasher()

    @Provides
    fun provideAuthRepository(
        dao: UserDao,
        passwordHasher: PasswordHasher
    ): AuthRepository = AuthRepositoryImpl(dao, passwordHasher)
}
