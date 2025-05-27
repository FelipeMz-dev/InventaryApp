package com.felipemz.inventaryapp.di

import com.felipemz.inventaryapp.data.local.AppDatabase
import org.koin.dsl.module

val daoModule = module {
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().productDao() }
    single { get<AppDatabase>().movementDao() }
    single { get<AppDatabase>().productCompositionDao() }
}