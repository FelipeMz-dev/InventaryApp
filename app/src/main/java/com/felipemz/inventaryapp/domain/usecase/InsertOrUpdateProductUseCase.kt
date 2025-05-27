package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.repository.CategoryRepository
import com.felipemz.inventaryapp.domain.repository.ProductRepository

class InsertOrUpdateProductUseCase(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: ProductModel) {
        categoryRepository.getById(product.category.id)?.run {
            productRepository.insertOrUpdate(product)
        }
    }
}