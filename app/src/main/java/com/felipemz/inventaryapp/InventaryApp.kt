package com.felipemz.inventaryapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InventaryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@InventaryApp)
            modules(appModule)
        }
    }
}