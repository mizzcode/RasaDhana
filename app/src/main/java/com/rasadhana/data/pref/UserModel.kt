package com.rasadhana.data.pref

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
    val photo: String = "",
    val expireToken: String
)