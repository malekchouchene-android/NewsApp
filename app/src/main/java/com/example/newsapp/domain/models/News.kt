package com.example.newsapp.domain.models

import java.util.*

data class News(
    val title: String,
    val imageUrl: String?,
    val description: String?,
    val redirectLink: String?,
    val source: String?,
    val publishedAt: Date?
)
