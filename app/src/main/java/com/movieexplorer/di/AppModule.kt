package com.movieexplorer.di

import android.content.Context
import androidx.room.Room
import com.movieexplorer.authentication.data.local.AppDatabase
import com.movieexplorer.authentication.data.local.UserDao
//import com.movieexplorer.auth.data.local.SessionManager
import com.movieexplorer.auth.data.repository.SessionRepositoryImpl
import com.movieexplorer.auth.data.security.BCryptPasswordHasher
import com.movieexplorer.auth.data.security.SecureSessionManager
//import com.movieexplorer.auth.data.security.Sha256PasswordHasher
import com.movieexplorer.auth.domain.security.PasswordHasher
import com.movieexplorer.auth.domain.session.SessionRepository
import com.movieexplorer.authentication.data.repository.AuthRepositoryImpl
import com.movieexplorer.authentication.domain.repository.AuthRepository
import com.movieexplorer.auth.domain.usecase.LoginUseCase
import com.movieexplorer.auth.domain.usecase.LogoutUseCase
import com.movieexplorer.authentication.domain.use_case.CheckSessionUseCase
import com.movieexplorer.authentication.domain.use_case.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //The dependencies in this module live as long as the application.
object AppModule { //Why object and not class? Because Only one AppModule is needed

    // 🟦 DATABASE
    @Provides  //Hilt can use this function to create an object.
    @Singleton //Create only ONE instance
    fun provideDatabase(
        @ApplicationContext context: Context //inside  mainActivity before hilt val context = this Now Hilt automatically gives you the application Context
    ): AppDatabase {
        return Room.databaseBuilder( //Exactly the same code you had in MainActivity Hilt just moved it into AppModule
            context,
            AppDatabase::class.java,
            "movie_db"
        ).build()
    }

    // 🟦 DAO
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao { //Ho;t sees that you needs AppDatabase and then call provider AppDatabase automatically
        return db.userDao()  //same as old code val dao = db.userDao()
    }

    // 🟦 SESSION MANAGER
   /* @Provides
    @Singleton
    fun provideSessionManager(  //Same as val sessionManager = SessionManager(context) that was in mainActivity
        @ApplicationContext context: Context
    ): SessionManager {
        return SessionManager(context)
    }*/
    @Provides
    @Singleton
    fun provideSecureSessionManager(
        @ApplicationContext context: Context
    ): SecureSessionManager {
        return SecureSessionManager(context)
    }

    // 🟦 SESSION REPOSITORY
    @Provides  // provider is basically replacing one line that used to be in your MainActivity which is val =
    fun provideSessionRepository(
        sessionManager: SecureSessionManager
    ): SessionRepository {
        return SessionRepositoryImpl(sessionManager)
    }

    @Provides
    @Singleton
    fun providePasswordHasher(): PasswordHasher {
        return BCryptPasswordHasher()
    }

    // 🟦 AUTH REPOSITORY
    @Provides
    fun provideAuthRepository(
        dao: UserDao,
        passwordHasher: PasswordHasher
    ): AuthRepository {
        return AuthRepositoryImpl(
            dao,
            passwordHasher
        )
    }

    // 🟦 USE CASES
    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        sessionRepository: SessionRepository
    ): LoginUseCase {
        return LoginUseCase(authRepository, sessionRepository)
    }

    @Provides
    fun provideSignUpUseCase(   //Returns SignUpUseCase(authRepository)
        authRepository: AuthRepository
    ): SignUpUseCase {
        return SignUpUseCase(authRepository)
    }

    @Provides
    fun provideCheckSessionUseCase(
        sessionRepository: SessionRepository
    ): CheckSessionUseCase {
        return CheckSessionUseCase(sessionRepository)
    }

    @Provides
    fun provideLogoutUseCase(
        sessionRepository: SessionRepository
    ): LogoutUseCase {
        return LogoutUseCase(sessionRepository)
    }
}
/*
Suppose LoginViewModel needs:loginUseCase
Hilt thinks
Need LoginUseCase
    ↓
Need AuthRepository
    ↓
Need UserDao
    ↓
Need AppDatabase
    ↓
Need Context
Then it builds them in the correct order automatically
*/