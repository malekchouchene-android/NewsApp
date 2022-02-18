package com.example.newsapp.ui.newsDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.newsapp.R
import com.example.newsapp.domain.models.News
import com.example.newsapp.ui.newslist.NewsImageCompose
import com.example.newsapp.ui.newslist.NewsTitle
import com.example.newsapp.ui.theme.Shapes
import com.example.newsapp.ui.theme.Typography

@Composable
fun NewsDetailsCompose(news: News, onSeeMoreClicked: (String) -> Unit, onBack: () -> Unit) {
    val scrollState = rememberScrollState()
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = news.title, maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            elevation = 2.dp,
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        )
    }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState, true),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NewsImageCompose(
                    url = news.imageUrl,
                    modifier = Modifier
                        .clip(Shapes.medium),
                    placeholder = ContextCompat.getDrawable(
                        LocalContext.current,
                        R.drawable.news_image_placeholder
                    )
                )
                NewsTitle(news = news, modifier = Modifier.fillMaxWidth())
                Text(
                    text = news.description ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    style = Typography.body1
                )
                if (news.redirectLink != null) {
                    TextButton(onClick = {
                        onSeeMoreClicked(news.redirectLink)
                    }) {
                        Text(text = stringResource(id = R.string.see_more))
                    }
                }
            }
        }
    }
}
