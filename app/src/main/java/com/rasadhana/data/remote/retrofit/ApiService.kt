package com.rasadhana.data.remote.retrofit

import com.rasadhana.data.remote.response.FileUploadResponse
import com.rasadhana.data.remote.response.HomeResponse
import com.rasadhana.data.remote.response.LoginResponse
import com.rasadhana.data.remote.response.OtpResponse
import com.rasadhana.data.remote.response.RegisterResponse
import com.rasadhana.data.remote.response.RegisterVerifyOtpResponse
import com.rasadhana.data.remote.response.ResetPasswordResponse
import com.rasadhana.data.remote.response.UpdateUserResponse
import com.rasadhana.data.remote.response.UserDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("auth/verify-register")
    suspend fun registerVerify(
        @Field("email") email: String,
        @Field("otp") otp: String,
    ) : RegisterVerifyOtpResponse

    @FormUrlEncoded
    @POST("auth/login-user")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("auth/userdata")
    suspend fun getUserData(
        @Header("Authorization") token: String
    ) : UserDataResponse

    @FormUrlEncoded
    @POST("auth/forgot-password")
    suspend fun getOtpForgotPassword(
        @Field("email") email: String
    ) : OtpResponse

    @FormUrlEncoded
    @POST("auth/reset-password")
    suspend fun updatePassword(
        @Field("otp") otp: String,
        @Field("newPassword") newPassword: String
    ) : ResetPasswordResponse

    @Multipart
    @POST("photos/upload-photo")
    suspend fun uploadImage(
        @Part photo: MultipartBody.Part,
        @Part("userId") userId: RequestBody
    ) : FileUploadResponse

    @GET("recipes/allrecipe")
    suspend fun getAllRecipe() : HomeResponse

    @Multipart
    @PATCH("auth/update/{userId}")
    suspend fun updateUserData(
        @Part photo: MultipartBody.Part?,
        @Part("name") name: RequestBody?,
        @Path("userId") userId: String,
    ) : UpdateUserResponse
}