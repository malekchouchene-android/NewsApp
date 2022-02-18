package com.example.newsapp.ui.newslist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.newsapp.ui.newsDetails.NewsDetailsActivity
import com.example.newsapp.ui.theme.NewsAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsListActivity : ComponentActivity() {
    private val viewModel: NewsListViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                NewsListScreenCompose(
                    viewModel = viewModel,
                    onNewsClicked = {
                        startActivity(NewsDetailsActivity.createIntent(this, it))
                    },
                    onErrorRetry = {
                        viewModel.retryGetNewsList()
                    },
                    onRefreshNewsList = {
                        viewModel.retryGetNewsList()
                    }
                )
            }
        }
    }
}
