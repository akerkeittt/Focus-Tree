package com.example.project.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class TranslateRequest(val q: String, val source: String = "auto", val target: String)
data class TranslateResponse(val translatedText: String)

interface TranslateApiService {
    @POST("translate")
    @Headers("Content-Type: application/json")
    suspend fun translate(
        @Body request: TranslateRequest
    ): List<TranslateResponse>
}


object TranslateApi {
    private const val BASE_URL = "https://libretranslate.com/"

    val instance: TranslateApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateApiService::class.java)
    }
}
