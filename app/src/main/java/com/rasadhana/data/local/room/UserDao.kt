package com.rasadhana.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rasadhana.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE _id = :id")
    fun getUserById(id: String): LiveData<UserEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)
}