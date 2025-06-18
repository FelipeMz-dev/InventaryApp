package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.domain.repository.ProductRepository

class SortProductsFromObserver(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        orderBy: ProductsOrderBy,
        isInverted: Boolean = false
    ) = repository.sortProductsFromObserver(orderBy, isInverted)
}