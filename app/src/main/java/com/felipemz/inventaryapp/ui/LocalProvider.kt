package com.felipemz.inventaryapp.ui

import androidx.compose.runtime.compositionLocalOf
import com.felipemz.inventaryapp.domain.model.ProductModel

val LocalProductList = compositionLocalOf<List<ProductModel>> { emptyList() }