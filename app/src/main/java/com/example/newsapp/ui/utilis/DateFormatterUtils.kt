package com.example.newsapp.ui.utilis

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

const val DateTimeFormatJson = "yyyy-MM-dd'T'HH:mm:ss'Z'"

fun String?.toLocalDate(dateFormat: String = DateTimeFormatJson): Date? {
    return this?.let {
        try {
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            simpleDateFormat.parse(it)
        } catch (e: Throwable) {
            Log.e("toLocalDate", e.toString())
            null
        }
    }
}

fun Date.formatSting(dateFormat: String): String {
    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    return simpleDateFormat.format(this)
}
