package com.rasadhana.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.rasadhana.data.Result
import com.rasadhana.data.local.entity.UserEntity
import com.rasadhana.data.local.room.UserDao
import com.rasadhana.data.pref.UserModel
import com.rasadhana.data.pref.UserPreference
import com.rasadhana.data.remote.response.LoginResponse
import com.rasadhana.data.remote.response.OtpResponse
import com.rasadhana.data.remote.response.RegisterResponse
import com.rasadhana.data.remote.response.ResetPasswordResponse
import com.rasadhana.data.remote.response.UserDataResponse
import com.rasadhana.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository(private val apiService: ApiService, private val userPreference: UserPreference, private val userDao: UserDao) {
    fun register(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    fun getUserData(token: String): LiveData<Result<UserEntity>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getUserData("Bearer $token")
            val data = response.data.let { data ->
                UserEntity(
                    id = data.id,
                    name = data.name,
                    email = data.email,
                    password = data.password
                )
            }

            userDao.insertUser(data)

            val localData: LiveData<Result<UserEntity>> = userDao.getUserById(data.id).map { Result.Success(it) }
            emitSource(localData)
        } catch (e: HttpException) {
            // Parse error body from API
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserDataResponse::class.java)
            emit(Result.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getOtp(email: String) = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getOtp(email)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, OtpResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    fun updatePassword(otp: String, newPassword: String) = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.updatePassword(otp, newPassword)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResetPasswordResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }
}