package com.felipemz.inventaryapp.di

import com.felipemz.inventaryapp.ui.home.HomeViewModel
import com.felipemz.inventaryapp.ui.product_form.ProductFormViewModel
import com.felipemz.inventaryapp.ui.movements.MovementsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProductFormViewModel)
    viewModelOf(::MovementsViewModel)
}