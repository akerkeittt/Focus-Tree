package com.example.project.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class QuoteResponse(
    val q: String,
    val a: String
)

interface QuoteApiService {
    @GET("random")
    suspend fun getRandomQuote(): List<QuoteResponse>
}


object QuoteApi {
    private const val BASE_URL = "https://zenquotes.io/api/"

    val instance: QuoteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApiService::class.java)
    }
}
