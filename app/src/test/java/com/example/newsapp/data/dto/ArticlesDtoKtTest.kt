package com.example.newsapp.data.dto

import com.example.newsapp.domain.models.News
import com.example.newsapp.ui.utilis.DateFormatNews
import com.google.common.truth.Truth
import org.junit.Test

class ArticlesDtoKtTest() {
    companion object {
        val articlesDto = Articles(
            title = "titre",
            source = null,
            urlToImage = "imageUrl",
            content = "",
            description = "description",
            publishedAt = "2022-02-18T07:23:24Z",
            author = "auteur",
            url = "url"
        )
        val expectedNews = News(
            title = "titre",
            imageUrl = "imageUrl",
            description = "description",
            publishedAt = "18/02/2022 07:23",
            redirectLink = "url"
        )
    }

    @Test
    fun should_map_article_dto() {

        Truth.assertThat(expectedNews).isEqualTo(articlesDto.toDomain(DateFormatNews))
    }

    @Test
    fun should_drop_articles_with_title_blank() {
        val articleDto = articlesDto.copy(title = "")
        Truth.assertThat(articleDto.toDomain("")).isNull()
    }

    @Test
    fun should_drop_articles_with_title_empty() {
        val articleDto = articlesDto.copy(title = " ")
        Truth.assertThat(articleDto.toDomain("")).isNull()
    }

    @Test
    fun should_drop_articles_with_title_null() {
        val articleDto = articlesDto.copy(title = null)
        Truth.assertThat(articleDto.toDomain("")).isNull()
    }
}
