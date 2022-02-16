package com.example.newsapp.data.network

import com.example.newsapp.data.dto.NewsApiResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesByCountry(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): NewsApiResponseDto
}
