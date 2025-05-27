package com.felipemz.inventaryapp.domain.repository

import com.felipemz.inventaryapp.domain.model.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun observeAllCategories(): Flow<List<CategoryModel>>

    suspend fun insertOrUpdate(category: CategoryModel) : CategoryModel?

    suspend fun getById(id: Int): CategoryModel?

    suspend fun delete(id: Int)
}