package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.repository.ProductRepository

class InsertProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: ProductModel) = repository.insertProduct(product)
}