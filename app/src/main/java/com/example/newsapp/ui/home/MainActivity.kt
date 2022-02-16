package com.example.newsapp.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.newsapp.R
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.ui.utilis.formatError
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    val viewModel: NewsListViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                val scaffoldState = rememberScaffoldState()
                val scopeScaffold = rememberCoroutineScope()
                Scaffold(
                    scaffoldState = scaffoldState,
                    content = { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            color = MaterialTheme.colors.background
                        ) {
                            val uiState = viewModel.uiState.collectAsState()
                            val uiStateValue = uiState.value
                            if (uiStateValue.isInnit) {
                                LoaderFullScreen()
                            } else {
                                SwipeRefresh(
                                    state = rememberSwipeRefreshState(uiStateValue.isLoading),
                                    onRefresh = { viewModel.retryGetNewsList() }
                                ) {
                                    if (uiStateValue.throwable != null) {
                                        if (uiStateValue.news.isEmpty()) {
                                            ErrorStateCompose(
                                                uiStateValue.throwable.formatError()
                                            ) {
                                                viewModel.retryGetNewsList()
                                            }
                                        } else {
                                            NewsListCompose(news = uiState.value.news)
                                            scopeScaffold.launch {
                                                val snackBarResult =
                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                        message = getString(uiStateValue.throwable.formatError()),
                                                        actionLabel = getString(R.string.retry),
                                                        duration = SnackbarDuration.Long
                                                    )
                                                if (snackBarResult == SnackbarResult.ActionPerformed) {
                                                    viewModel.retryGetNewsList()
                                                }
                                            }
                                        }
                                    } else {
                                        if (uiStateValue.news.isEmpty()) {
                                            ErrorStateCompose(resString = R.string.empty_news_list) {
                                                viewModel.retryGetNewsList()
                                            }
                                        } else {
                                            NewsListCompose(uiStateValue.news)
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
