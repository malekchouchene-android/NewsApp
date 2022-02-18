package com.example.newsapp.ui.utilis

import java.text.SimpleDateFormat
import java.util.*

const val DateTimeFormatJson = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val DateFormatNews = "dd/MM/yyyy hh:mm"

fun String?.toLocalDate(dateFormat: String = DateTimeFormatJson): Date? {
    return this?.let {
        try {
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            simpleDateFormat.parse(it)
        } catch (e: Throwable) {
            null
        }
    }
}

fun Date.formatSting(dateFormat: String): String {
    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    return simpleDateFormat.format(this)
}
