package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveProductsNotPackaged(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<ProductModel>> {
        return repository.observeAllProductModels().map { products ->
            products.filter { product -> product.packageProducts.isNullOrEmpty() }
        }
    }
}