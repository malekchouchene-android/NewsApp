package com.example.newsapp.ui.newslist

import com.example.newsapp.data.dto.ArticlesDtoKtTest.Companion.expectedNews
import com.example.newsapp.domain.NewsList.NewsListUseCase
import com.example.newsapp.domain.models.News
import com.example.newsapp.ui.utilis.DateFormatNews
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NewsListViewModelTest {
    @Mock
    lateinit var useCase: NewsListUseCase
    lateinit var viewModel: NewsListViewModel
    val dispatcher = StandardTestDispatcher()

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        viewModel = NewsListViewModel(useCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun should_get_list_state() {
        runTest {
            Mockito.`when`(useCase.getNewListByCountry(Locale.FRANCE, DateFormatNews)).then {
                flow {
                    emit(Result.success(listOf(expectedNews)))
                }
            }
            viewModel.getNewsList()
            val value = viewModel.uiState.filter { !it.isInnit }.first()
            Truth.assertThat(value).isEqualTo(
                NewsListUiState(
                    listOf(expectedNews),
                    false,
                    false,
                    null
                )
            )
        }
    }

    @Test
    fun should_get_fail_state() {
        val throwable = Throwable("test")
        runTest {
            Mockito.`when`(useCase.getNewListByCountry(Locale.FRANCE, DateFormatNews)).then {
                flow {
                    emit(Result.failure<List<News>>(throwable))
                }
            }
            viewModel.getNewsList()
            val value = viewModel.uiState.filter { !it.isInnit }.first()
            Truth.assertThat(value).isEqualTo(
                NewsListUiState(
                    emptyList(),
                    false,
                    false,
                    throwable
                )
            )
        }
    }

    @Test
    fun should_get_have_last_news_when_error() {
        val throwable = Throwable("test")
        runTest {
            Mockito.`when`(useCase.getNewListByCountry(Locale.FRANCE, DateFormatNews)).then {
                flow {
                    emit(Result.success(listOf(expectedNews)))
                    emit(Result.failure<List<News>>(throwable))
                }
            }
            viewModel.getNewsList()
            val values = viewModel.uiState.take(2).toList()
            Truth.assertThat(values[1]).isEqualTo(
                NewsListUiState(
                    listOf(expectedNews),
                    false,
                    false,
                    throwable
                )
            )
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun should_have_allways_loading_when_retry_get_news() {

        runTest {
            Mockito.`when`(useCase.getNewListByCountry(Locale.FRANCE, DateFormatNews)).then {
                flow {
                    emit(Result.success(listOf(expectedNews)))
                }
            }
            viewModel.retryGetNewsList()
            val values = viewModel.uiState.take(2).toList()
            Truth.assertThat(values[0].isLoading).isEqualTo(
                true
            )
        }
    }
}
