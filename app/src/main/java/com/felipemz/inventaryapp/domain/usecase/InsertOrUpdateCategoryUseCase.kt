package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.repository.CategoryRepository

class InsertOrUpdateCategoryUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(category: CategoryModel): CategoryModel? {
        repository.getById(category.id)?.also {
            return repository.update(category)
        }
        return repository.insert(category)
    }
}