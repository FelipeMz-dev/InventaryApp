package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.repository.ProductRepository

class GetProductsIdAndNameFromCategoryUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(categoryId: Int): List<Pair<Int, String>> {
        return repository.getProductsIdFromCategoryId(categoryId).mapNotNull { productId ->
            repository.getNameById(productId)?.let { name ->
                Pair(productId, name)
            }
        }
    }
}