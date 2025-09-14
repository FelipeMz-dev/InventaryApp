package com.felipemz.inventaryapp.ui.commons.delegate

import com.felipemz.inventaryapp.core.extensions.orTrue
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ProductListFilterDelegate {
    val filteredProductList: StateFlow<List<ProductModel>>
    fun setProductList(products: List<ProductModel>)
    fun setFilterCategory(category: CategoryModel?)
    fun setFilterName(text: String?)
}

class ProductListFilterDelegateImpl: ProductListFilterDelegate {

    private var productList: List<ProductModel> = emptyList()
    private var selectedCategory: CategoryModel? = null
    private var searchName: String? = null
    private val _filteredProductList = MutableStateFlow<List<ProductModel>>(emptyList())

    override val filteredProductList: StateFlow<List<ProductModel>> get() = _filteredProductList.asStateFlow()

    private fun filterProducts() {
        _filteredProductList.value = productList.filter { product ->
            val matchesCategory = selectedCategory?.let {
                product.category.id == selectedCategory?.id
            }.orTrue()
            val matchesSearchName = searchName?.let {
                product.name.contains(it, ignoreCase = true)
            }.orTrue()
            matchesCategory && matchesSearchName
        }
    }

    override fun setProductList(products: List<ProductModel>) {
        productList = products
        filterProducts()
    }

    override fun setFilterCategory(category: CategoryModel?) {
        selectedCategory = category
        filterProducts()
    }

    override fun setFilterName(text: String?) {
        searchName = text
        filterProducts()
    }
}