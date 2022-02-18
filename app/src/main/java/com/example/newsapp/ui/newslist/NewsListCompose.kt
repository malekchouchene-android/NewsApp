package com.example.newsapp.ui.newslist

import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.newsapp.ui.utilis.formatError
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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
fun NewsListItem(news: News, onNewsClicked: ((News) -> Unit)?) {
    val placeHolder = ContextCompat.getDrawable(
        LocalContext.current,
        R.drawable.news_image_placeholder
    )
    Card(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onNewsClicked?.invoke(news)
            },
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
            NewsImageCompose(
                news.imageUrl,
                placeholder = placeHolder,
                modifier = Modifier.clip(
                    shape = Shapes.medium.copy(
                        bottomStart = CornerSize(0),
                        bottomEnd = CornerSize(0)
                    )
                )
            )
            NewsTitle(
                news = news,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            val dateOfPublishing =
                news.publishedAt
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
            "13/02/2022 15:30",
        )
    ) {
    }
}

@Composable
fun NewsListCompose(news: List<News>, onNewsClicked: ((News) -> Unit)?) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(news) {
            NewsListItem(news = it, onNewsClicked)
        }
    }
}

@Composable
fun NewsImageCompose(url: String?, placeholder: Drawable?, modifier: Modifier) {
    GlideImage(
        imageModel = url,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        placeHolder = rememberDrawablePainter(
            placeholder
        ),
        error = rememberDrawablePainter(
            placeholder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16F / 9F)
            .then(modifier)
    )
}

@Composable
fun NewsTitle(news: News, modifier: Modifier) {
    Text(
        text = news.title,
        modifier,
        style = Typography.h4
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowSnackBarError(
    scaffoldState: ScaffoldState,
    message: String,
    actionLabel: String,
    action: (() -> Unit)?
) {
    LaunchedEffect(scaffoldState) {
        val snackBarResult =
            scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Long
            )
        if (snackBarResult == SnackbarResult.ActionPerformed) {
            action?.invoke()
        }
    }
}

@Composable
fun NewsListScreenCompose(
    viewModel: NewsListViewModel,
    onNewsClicked: ((News) -> Unit),
    onErrorRetry: (() -> Unit),
    onRefreshNewsList: (() -> Unit)
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.news_list_title)) },
                elevation = 2.dp
            )
        },
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
                        onRefresh = { onRefreshNewsList.invoke() }
                    ) {
                        if (uiStateValue.throwable != null) {
                            if (uiStateValue.news.isEmpty()) {
                                ErrorStateCompose(
                                    uiStateValue.throwable.formatError()
                                ) {
                                    onErrorRetry.invoke()
                                }
                            } else {
                                NewsListCompose(news = uiState.value.news) {
                                    onNewsClicked.invoke(it)
                                }
                                ShowSnackBarError(
                                    scaffoldState = scaffoldState,
                                    message = stringResource(
                                        id =
                                        uiStateValue.throwable.formatError()
                                    ),
                                    actionLabel = stringResource(id = R.string.retry)
                                ) {
                                    onErrorRetry.invoke()
                                }
                            }
                        } else {
                            if (uiStateValue.news.isEmpty()) {
                                ErrorStateCompose(resString = R.string.empty_news_list) {
                                    onErrorRetry.invoke()
                                }
                            } else {
                                NewsListCompose(uiStateValue.news) {
                                    onNewsClicked.invoke(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
