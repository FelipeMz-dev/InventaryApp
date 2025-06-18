package com.felipemz.inventaryapp

import android.app.Application
import com.felipemz.inventaryapp.di.appModule
import com.felipemz.inventaryapp.di.daoModule
import com.felipemz.inventaryapp.di.repositoryModule
import com.felipemz.inventaryapp.di.useCaseModule
import com.felipemz.inventaryapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InventaryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@InventaryApp)
            modules(
                appModule,
                daoModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
            )
        }
    }
}