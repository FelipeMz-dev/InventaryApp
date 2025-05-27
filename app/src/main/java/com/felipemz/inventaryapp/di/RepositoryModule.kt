package com.felipemz.inventaryapp.di

import com.felipemz.inventaryapp.data.repository.CategoryRepositoryImpl
import com.felipemz.inventaryapp.data.repository.ProductRepositoryImpl
import com.felipemz.inventaryapp.domain.repository.CategoryRepository
import com.felipemz.inventaryapp.domain.repository.ProductRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
}