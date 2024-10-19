package com.tadevi.demo.app

import android.app.Application
import com.tadevi.demo.app.koin.appModule
import com.tadevi.demo.app.koin.useCaseModule
import com.tadevi.demo.app.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@App)
            modules(appModule, viewModelModule, useCaseModule)
        }
    }
}