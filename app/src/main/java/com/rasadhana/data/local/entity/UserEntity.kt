package com.rasadhana.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "User")
data class UserEntity (
    @PrimaryKey(autoGenerate = false)
    val id: String,

    val name: String,
    val email: String,
    val password: String = "",
    val photoUrl: String,
    val isLogin: Boolean,
    val token: String,
    val expireToken: String,
    val authType: String
) : Parcelable