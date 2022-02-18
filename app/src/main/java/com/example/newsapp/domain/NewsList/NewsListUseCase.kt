package com.example.newsapp.domain.NewsList

import com.example.newsapp.domain.models.News
import kotlinx.coroutines.flow.Flow
import java.util.*

class NewsListUseCase(private val newsListRepository: NewsListRepository) {
    suspend fun getNewListByCountry(locale: Locale, dateFormat: String): Flow<Result<List<News>>> {
        return newsListRepository.getNewsListByCountry(local = locale, dateFormat = dateFormat)
    }
}
