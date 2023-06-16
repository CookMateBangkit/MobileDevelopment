package com.example.cookmate.api

import com.example.cookmate.response.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @JvmSuppressWildcards
    @GET("recipes")
    suspend fun recipe(): RecipeResponse

    @JvmSuppressWildcards
    @GET("recipes/{id}")
    suspend fun recipeDetail(
        @Path("id")id: String,
    ): DetailRecipeResponse

    @JvmSuppressWildcards
    @GET("recipes")
    suspend fun searchResep(
        @QueryMap params: Map<String, Any>
    ): RecipeResponse

    @Multipart
    @POST("predict")
    suspend fun uploadPhoto(
        @Part file: MultipartBody.Part
    ): RecipeResponse

    @Headers("Content-Type: application/json")
    @POST("recommend")
    suspend fun uploadText(
        @Body requestBody: RequestBody
    ): RecipeResponse
}