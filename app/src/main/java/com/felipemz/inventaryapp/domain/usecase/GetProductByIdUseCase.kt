package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.repository.ProductRepository

class GetProductByIdUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int): ProductModel? = repository.getById(id)
}