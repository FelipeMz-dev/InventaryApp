package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.repository.CategoryRepository
import com.felipemz.inventaryapp.domain.repository.ProductRepository

class DeleteCategoryIfNotUseUseCase(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Boolean {
        if (productRepository.countProductsFromCategoryId(id) > 0) return false
        categoryRepository.delete(id)
        return true
    }
}