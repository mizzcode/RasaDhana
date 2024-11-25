package com.rasadhana.data.remote.retrofit

import com.rasadhana.data.remote.response.LoginResponse
import com.rasadhana.data.remote.response.OtpResponse
import com.rasadhana.data.remote.response.RegisterResponse
import com.rasadhana.data.remote.response.ResetPasswordResponse
import com.rasadhana.data.remote.response.UserDataResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("auth/login-user")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @POST("auth/userdata")
    suspend fun getUserData(
        @Header("Authorization") token: String
    ) : UserDataResponse

    @FormUrlEncoded
    @POST("auth/forgot-password")
    suspend fun getOtp(
        @Field("email") email: String
    ) : OtpResponse

    @FormUrlEncoded
    @POST("auth/reset-password")
    suspend fun updatePassword(
        @Field("otp") otp: String,
        @Field("newPassword") newPassword: String
    ) : ResetPasswordResponse
}