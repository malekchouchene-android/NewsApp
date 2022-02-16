package com.example.newsapp.domain.NewsList

import com.example.newsapp.domain.models.News
import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface NewsListRepository {
    suspend fun getNewsListByCountry(local: Locale): Flow<Result<List<News>>>
}
