package com.example.newsapp.data.dto

import com.example.newsapp.domain.models.News
import com.example.newsapp.ui.utilis.formatSting
import com.example.newsapp.ui.utilis.toLocalDate
import com.google.gson.annotations.SerializedName

data class Articles(

    @SerializedName("source") val source: SourceDto?,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("content") val content: String?
)

fun Articles.toDomain(dateFormat: String): News? {
    if (title.isNullOrBlank()) return null
    return News(
        title = title,
        imageUrl = urlToImage,
        redirectLink = url,
        description = description,
        publishedAt = publishedAt?.toLocalDate()?.formatSting(dateFormat),
    )
}
