package com.movieexplorer.authentication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.movieexplorer.auth.data.local.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}