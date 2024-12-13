package com.rasadhana.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rasadhana.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE id = :id")
    fun getUserById(id: String): LiveData<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

}