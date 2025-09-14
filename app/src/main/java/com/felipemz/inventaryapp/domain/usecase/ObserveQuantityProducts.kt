package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveQuantityProducts(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<ProductModel>> {
        return repository.observeAllProductModels().map { products ->
            products.filter {
                it.packageProducts.isNullOrEmpty() && it.quantityModel.isNotNull()
            }
        }
    }
}