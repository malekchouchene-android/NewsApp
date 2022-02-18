package com.example.newsapp.data.repositories

import com.example.newsapp.data.dto.toDomain
import com.example.newsapp.data.network.NewsApi
import com.example.newsapp.domain.NewsList.NewsListRepository
import com.example.newsapp.domain.models.News
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*

class NewsListRepositoryImp(
    val newsApi: NewsApi,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NewsListRepository {
    companion object {
        const val apiKey = "64a20f48f80a47a4a4a8013e9f6e5cfd"
    }

    override suspend fun getNewsListByCountry(
        local: Locale,
        dateFormat: String
    ): Flow<Result<List<News>>> {
        return flow { emit(newsApi.getTopHeadlinesByCountry(local.country, apiKey)) }
            .map {
                it.articles.mapNotNull { articles ->
                    articles.toDomain(dateFormat = dateFormat)
                }
            }.map {
                Result.success(it)
            }
            .flowOn(backgroundDispatcher)
            .catch { t ->
                emit(Result.failure(t))
            }
    }
}
