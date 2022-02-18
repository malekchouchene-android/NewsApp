package com.example.newsapp.ui.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.NewsList.NewsListUseCase
import com.example.newsapp.domain.models.News
import com.example.newsapp.ui.utilis.DateFormatNews
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class NewsListViewModel(private val newsListUseCase: NewsListUseCase) : ViewModel() {
    companion object {
        val TAG_LOG = NewsListViewModel::class.simpleName
    }

    private val _uiState: MutableStateFlow<NewsListUiState> =
        MutableStateFlow(NewsListUiState(emptyList(), false, true, null))
    val uiState: StateFlow<NewsListUiState> = _uiState

    init {
        getNewsList()
    }

    fun getNewsList() {
        viewModelScope.launch {
            newsListUseCase.getNewListByCountry(Locale.FRANCE, DateFormatNews)
                .collect { result ->
                    result.onSuccess {
                        _uiState.value = NewsListUiState(it, false, false, null)
                    }
                    result.onFailure {
                        val lastUiState = _uiState.value
                        _uiState.value =
                            lastUiState.copy(throwable = it, isInnit = false, isLoading = false)
                    }
                }
        }
    }

    fun retryGetNewsList() {
        val lastUiState = _uiState.value
        _uiState.value = lastUiState.copy(isLoading = true, isInnit = false)
        getNewsList()
    }
}

data class NewsListUiState(
    val news: List<News>,
    val isLoading: Boolean,
    val isInnit: Boolean,
    val throwable: Throwable?
)
