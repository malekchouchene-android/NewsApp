package com.example.newsapp.ui.newslist

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.newsapp.R
import com.example.newsapp.domain.models.News
import com.example.newsapp.ui.theme.Shapes
import com.example.newsapp.ui.theme.Typography
import com.example.newsapp.ui.utilis.formatSting
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun LoaderFullScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
@Preview
fun LoaderFullScreenPreview() {
    LoaderFullScreen()
}

@Composable
fun ErrorStateCompose(@StringRes resString: Int, retryAction: (() -> Unit)?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(resString))
        if (retryAction != null) {
            Button(
                onClick = { retryAction() },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Composable
@Preview
fun ErrorStateComposePreview() {
    ErrorStateCompose(R.string.no_network_error) {
    }
}

@Composable
fun NewsListItem(news: News) {
    val placeHolder = ContextCompat.getDrawable(
        LocalContext.current,
        R.drawable.news_image_placeholder
    )
    Card(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = Shapes.medium,
        elevation = 4.dp
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(
                8.dp
            )
        ) {
            GlideImage(
                imageModel = news.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                placeHolder = rememberDrawablePainter(
                    placeHolder
                ),
                error = rememberDrawablePainter(
                    placeHolder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp)
                    .clip(
                        Shapes.medium.copy(
                            bottomEnd = CornerSize(0),
                            bottomStart = CornerSize(0)
                        )
                    )

            )
            Text(
                text = news.title,
                Modifier.padding(horizontal = 8.dp),
                style = Typography.body1
            )

            val dateOfPublishing =
                news.publishedAt?.formatSting("dd/MM/yyyy hh:mm")
            if (dateOfPublishing != null) {
                Text(
                    text = stringResource(
                        id = R.string.published_at,
                        formatArgs = arrayOf(dateOfPublishing)
                    ),
                    style = Typography.body2,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewNewsListItem() {
    NewsListItem(
        News(
            "test titre ",
            imageUrl = "test",
            description = null,
            null,
            null,
            null
        )
    )
}

@Composable
fun NewsListCompose(news: List<News>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(news) {
            NewsListItem(news = it)
        }
    }
}
