package com.felipemz.inventaryapp

import com.felipemz.inventaryapp.ui.home.HomeViewModel
import com.felipemz.inventaryapp.ui.product.ProductFormViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProductFormViewModel)
}