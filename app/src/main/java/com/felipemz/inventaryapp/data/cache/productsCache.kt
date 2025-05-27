package com.felipemz.inventaryapp.data.cache

import com.felipemz.inventaryapp.domain.model.ProductModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object ProductsCache {

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: Flow<List<ProductModel>> = _products

    fun setProducts(newProducts: List<ProductModel>) {
        _products.value = newProducts
    }
}