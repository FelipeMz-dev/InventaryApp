package com.felipemz.inventaryapp.data.cache

import com.felipemz.inventaryapp.domain.model.CategoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object CategoriesCache {

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: Flow<List<CategoryModel>> = _categories

    fun setCategories(newCategories: List<CategoryModel>) {
        _categories.value = newCategories
    }
}