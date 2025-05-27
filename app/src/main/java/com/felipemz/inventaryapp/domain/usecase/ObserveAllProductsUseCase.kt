package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<ProductModel>> = repository.observeAllProductModels()
}