package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllCategoriesUseCase(
    private val repo: CategoryRepository
) {
    operator fun invoke(): Flow<List<CategoryModel>> = repo.observeAllCategories()
}