package com.felipemz.inventaryapp.domain.repository

import com.felipemz.inventaryapp.domain.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<ProductModel>>
    suspend fun getProductById(id: Int): ProductModel?
    suspend fun insertProduct(product: ProductModel)
}