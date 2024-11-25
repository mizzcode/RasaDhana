package com.rasadhana.data.repository

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.rasadhana.data.Result
import com.rasadhana.data.pref.UserModel
import com.rasadhana.data.pref.UserPreference
import com.rasadhana.data.remote.response.RegisterResponse
import com.rasadhana.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository(private val apiService: ApiService, private val userPreference: UserPreference) {
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
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    fun findOneUser(token: String) = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.findOneUser("Bearer $token")
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
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