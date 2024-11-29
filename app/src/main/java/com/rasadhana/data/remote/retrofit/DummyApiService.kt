package com.rasadhana.data.remote.retrofit

import com.rasadhana.data.remote.response.RecipeResponseItem
import retrofit2.http.GET

interface DummyApiService {
    @GET("Sheet1")
    suspend fun getDummyRecipes(): List<RecipeResponseItem>
}