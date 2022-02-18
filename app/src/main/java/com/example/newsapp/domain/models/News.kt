package com.example.newsapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val title: String,
    val imageUrl: String?,
    val description: String?,
    val redirectLink: String?,
    val publishedAt: String?
) : Parcelable
