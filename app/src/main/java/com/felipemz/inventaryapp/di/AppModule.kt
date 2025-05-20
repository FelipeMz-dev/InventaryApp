package com.felipemz.inventaryapp.di

import androidx.room.Room
import com.felipemz.inventaryapp.core.DATABASE_NAME
import com.felipemz.inventaryapp.data.local.AppDatabase
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}