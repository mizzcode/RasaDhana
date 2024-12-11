package com.rasadhana.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
    val photo: String = "",
    val expireToken: String
) : Parcelable