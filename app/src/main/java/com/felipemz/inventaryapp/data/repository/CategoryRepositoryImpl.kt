package com.felipemz.inventaryapp.data.repository

import com.felipemz.inventaryapp.data.cache.CategoriesCache
import com.felipemz.inventaryapp.data.local.dao.CategoryDao
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.repository.CategoryRepository
import com.felipemz.inventaryapp.domain.repository.mapper.toEntity
import com.felipemz.inventaryapp.domain.repository.mapper.toModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    private val categoryCache = CategoriesCache
    private var canObserveCategories = true

    override fun observeAllCategories(): Flow<List<CategoryModel>> {
        if (canObserveCategories) initCategoryObservation()
        return categoryCache.categories
    }

    private fun initCategoryObservation() {
        canObserveCategories = false
        categoryDao.observeAll()
            .map { list -> list.map { it.toModel() } }
            .onEach { categoryCache.setCategories(it) }
            .launchIn(CoroutineScope(Dispatchers.IO))
    }

    override suspend fun insertOrUpdate(category: CategoryModel): CategoryModel? {
        categoryDao.getById(category.id)?.apply {
            categoryDao.update(category.toEntity())
            return category
        }
        val newCategoryId = categoryDao.insert(category.toEntity()).toInt()
        return categoryDao.getById(newCategoryId)?.toModel()
    }

    override suspend fun getById(id: Int): CategoryModel? {
        return categoryDao.getById(id)?.toModel()
    }

    override suspend fun delete(id: Int) {
        categoryDao.getById(id)?.let { categoryDao.delete(it) }
    }
}