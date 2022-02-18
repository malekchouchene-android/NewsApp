package com.example.newsapp.ui.newsDetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import com.example.newsapp.domain.models.News
import com.example.newsapp.ui.theme.NewsAppTheme

class NewsDetailsActivity : ComponentActivity() {
    companion object {
        const val NEWS_ITEM_KEY = "news_item_key"
        fun createIntent(context: Context, news: News): Intent {
            val intent = Intent(context, NewsDetailsActivity::class.java)
            intent.putExtra(NEWS_ITEM_KEY, news)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val news = intent.getParcelableExtra<News>(NEWS_ITEM_KEY)
        news?.let {
            setContent {
                NewsAppTheme {
                    NewsDetailsCompose(
                        news = it,
                        onSeeMoreClicked = { url ->
                            CustomTabsIntent.Builder()
                                .build().launchUrl(this, Uri.parse(url))
                        },
                        onBack = {
                            onBackPressed()
                        }
                    )
                }
            }
        } ?: kotlin.run {
            finish()
        }
    }
}
