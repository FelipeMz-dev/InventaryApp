package com.felipemz.inventaryapp.data.cache

import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.domain.model.ProductModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

object ProductsCache {

    val orderBy = MutableStateFlow(ProductsOrderBy.ID)
    val isInverted = MutableStateFlow(false)

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: Flow<List<ProductModel>> = combine(
        orderBy,
        isInverted,
        _products
    ) { order, inverted, products ->
        getSortedProducts(
            order = order,
            inverted = inverted,
            products = products
        )
    }

    private fun getSortedProducts(
        order: ProductsOrderBy,
        inverted: Boolean,
        products: List<ProductModel>
    ) = when (order) {
        ProductsOrderBy.ID -> products.sortedBy { it.id }
        ProductsOrderBy.NAME -> products.sortedBy { it.name }
        ProductsOrderBy.CATEGORY -> products.sortedBy { it.category.name }
        ProductsOrderBy.PRICE -> products.sortedBy { it.price }
        ProductsOrderBy.STOCK -> products.sortedByDescending { it.quantityModel?.quantity }
    }.let { if (inverted) it.reversed() else it }

    fun setProducts(newProducts: List<ProductModel>) {
        _products.value = newProducts
    }

    fun sortBy(
        newOrderBy: ProductsOrderBy,
        isInverted: Boolean
    ) {
        orderBy.value = newOrderBy
        this.isInverted.value = isInverted
    }
}