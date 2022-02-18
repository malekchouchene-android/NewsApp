package com.example.newsapp.data.repositories

import com.example.newsapp.data.dto.ArticlesDtoKtTest.Companion.articlesDto
import com.example.newsapp.data.dto.ArticlesDtoKtTest.Companion.expectedNews
import com.example.newsapp.data.dto.NewsApiResponseDto
import com.example.newsapp.data.network.NewsApi
import com.example.newsapp.ui.utilis.DateFormatNews
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class NewsListRepositoryImpTest() {

    @Mock
    lateinit var api: NewsApi
    lateinit var repositoryImp: NewsListRepositoryImp

    @Before
    fun setup() {
        repositoryImp = NewsListRepositoryImp(api)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun should_return_result_with_mapped_news() {
        runTest {
            Mockito.`when`(api.getTopHeadlinesByCountry("FR", NewsListRepositoryImp.apiKey))
                .then {
                    NewsApiResponseDto(
                        status = "200",
                        totalResults = 1,
                        articles = listOf(
                            articlesDto
                        )
                    )
                }

            val result = repositoryImp.getNewsListByCountry(Locale.FRANCE, DateFormatNews)
                .first()
            Truth.assertThat(result.isSuccess).isTrue()
            Truth.assertThat(result.getOrNull()).isEqualTo(listOf(expectedNews))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun should_return_result_failure_when_ws_fail() {
        runTest {
            Mockito.`when`(api.getTopHeadlinesByCountry("FR", NewsListRepositoryImp.apiKey))
                .then {
                    throw HttpException(
                        Response.error<NewsApiResponseDto>(
                            404,
                            "Not found".toResponseBody()
                        )
                    )
                }

            val result = repositoryImp.getNewsListByCountry(Locale.FRANCE, DateFormatNews)
                .first()
            Truth.assertThat(result.isFailure).isTrue()
            Truth.assertThat(result.exceptionOrNull()).isInstanceOf(HttpException::class.java)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun should_return_result_succes_even_date_not_valid() {
        runTest {
            Mockito.`when`(api.getTopHeadlinesByCountry("FR", NewsListRepositoryImp.apiKey))
                .then {
                    NewsApiResponseDto(
                        status = "200",
                        totalResults = 1,
                        articles = listOf(
                            articlesDto.copy(publishedAt = "2O22")
                        )
                    )
                }

            val result = repositoryImp.getNewsListByCountry(Locale.FRANCE, DateFormatNews)
                .first()
            Truth.assertThat(result.isSuccess).isTrue()
            Truth.assertThat(result.getOrNull())
                .isEqualTo(listOf(expectedNews.copy(publishedAt = null)))
        }
    }
}
