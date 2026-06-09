package com.movieexplorer.authentication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.movieexplorer.auth.data.local.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?
}