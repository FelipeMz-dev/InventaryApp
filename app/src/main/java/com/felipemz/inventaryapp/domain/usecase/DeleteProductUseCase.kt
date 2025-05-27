package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.repository.ProductRepository

class DeleteProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteById(id)
}