package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.repository.CategoryRepository

class InsertOrUpdateCategoryUseCase(private val repo: CategoryRepository) {
    suspend operator fun invoke(category: CategoryModel): CategoryModel? = repo.insertOrUpdate(category)
}