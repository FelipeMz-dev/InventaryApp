package com.felipemz.inventaryapp.di

import com.felipemz.inventaryapp.domain.usecase.DeleteCategoryIfNotUseUseCase
import com.felipemz.inventaryapp.domain.usecase.DeleteProductUseCase
import com.felipemz.inventaryapp.domain.usecase.GetAllProductsUseCase
import com.felipemz.inventaryapp.domain.usecase.GetProductByIdUseCase
import com.felipemz.inventaryapp.domain.usecase.GetProductsIdAndNameFromCategoryUseCase
import com.felipemz.inventaryapp.domain.usecase.InsertOrUpdateCategoryUseCase
import com.felipemz.inventaryapp.domain.usecase.InsertOrUpdateProductUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveAllCategoriesUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveAllProductsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetAllProductsUseCase(get()) }
    factory { InsertOrUpdateProductUseCase(get(), get()) }
    factory { ObserveAllProductsUseCase(get()) }
    factory { GetProductsIdAndNameFromCategoryUseCase(get()) }
    factory { InsertOrUpdateCategoryUseCase(get()) }
    factory { ObserveAllCategoriesUseCase(get()) }
    factory { DeleteCategoryIfNotUseUseCase(get(), get()) }
    factory { DeleteProductUseCase(get()) }
    factory { GetProductByIdUseCase(get()) }
}