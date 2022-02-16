package com.example.newsapp.ui

import android.app.Application
import com.example.newsapp.di.dataModule
import com.example.newsapp.di.domainModule
import com.example.newsapp.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@NewsApplication)
            modules(*arrayOf(uiModule, dataModule, domainModule))
        }
    }
}
