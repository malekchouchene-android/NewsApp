package com.example.newsapp.data.dto

import com.google.gson.annotations.SerializedName

data class NewsApiResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int?,
    @SerializedName("articles") val articles: List<Articles>
)
