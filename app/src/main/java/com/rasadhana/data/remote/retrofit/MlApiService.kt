package com.rasadhana.data.remote.retrofit

import com.rasadhana.data.remote.response.RecipeResponse
import retrofit2.http.POST
import retrofit2.http.Path

interface MlApiService {
    @POST("predict_latest_image/{user_id}")
    suspend fun generateRecipes(
        @Path("user_id") userId: String
    ) : RecipeResponse
}