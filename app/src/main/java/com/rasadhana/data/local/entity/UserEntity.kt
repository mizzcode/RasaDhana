package com.rasadhana.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
class UserEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("_id")
    val id: String,

    val name: String,
    val email: String,
    val password: String,
    val photoUrl: String,
    val expireToken: String
)