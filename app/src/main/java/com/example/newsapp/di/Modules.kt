package com.example.newsapp.di

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.network.NewsApi
import com.example.newsapp.data.repositories.NewsListRepositoryImp
import com.example.newsapp.domain.NewsList.NewsListRepository
import com.example.newsapp.domain.NewsList.NewsListUseCase
import com.example.newsapp.ui.newslist.NewsListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else
            HttpLoggingInterceptor.Level.NONE

    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(7, TimeUnit.SECONDS)
        .connectTimeout(7, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()
    single<NewsApi> {
        Retrofit.Builder()
            .baseUrl(" https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(NewsApi::class.java)
    }
    single<NewsListRepository> {
        NewsListRepositoryImp(newsApi = get())
    }
}

val domainModule = module {
    single<NewsListUseCase> {
        NewsListUseCase(get())
    }
}

val uiModule = module {
    viewModel<NewsListViewModel> {
        NewsListViewModel(get())
    }
}
