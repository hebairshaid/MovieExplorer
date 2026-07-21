package com.movieexplorer.authentication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movieexplorer.auth.data.local.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email ORDER BY id DESC LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("UPDATE users SET password = :hashedPassword WHERE id = :userId")
    suspend fun updatePasswordById(userId: Int, hashedPassword: String): Int

    @Query("UPDATE users SET password = :hashedPassword WHERE email = :email")
    suspend fun updatePasswordByEmail(email: String, hashedPassword: String): Int
}
