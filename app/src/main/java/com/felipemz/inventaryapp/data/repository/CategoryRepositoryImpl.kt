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

    override suspend fun insert(category: CategoryModel): CategoryModel? {
        val position = categoryDao.getMaxPosition() + 1
        val newId = categoryDao.insert(category.copy(position = position).toEntity()).toInt()
        return categoryDao.getById(newId)?.toModel()
    }

    override suspend fun update(category: CategoryModel): CategoryModel? {
        categoryDao.update(category.toEntity())
        return category
    }

    override suspend fun getById(id: Int): CategoryModel? {
        return categoryDao.getById(id)?.toModel()
    }

    override suspend fun delete(id: Int) {
        categoryDao.getById(id)?.let {
            moveDownGreaterCategories(it.position)
            categoryDao.delete(it)
        }
    }

    private suspend fun moveDownGreaterCategories(position: Int) {
        val categoriesToMove = categoryDao.getCategoriesWithPositionGreaterThan(position)
        categoryDao.updateAll(categoriesToMove.map { it.copy(position = it.position - 1) })
    }
}