package com.felipemz.inventaryapp.domain.repository

import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.domain.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun observeAllProductModels(): Flow<List<ProductModel>>

    suspend fun sortProductsFromObserver(
        orderBy: ProductsOrderBy,
        isInverted: Boolean
    )

    suspend fun insertOrUpdate(product: ProductModel)

    suspend fun getById(id: Int): ProductModel?

    suspend fun deleteById(id: Int)

    fun getAllProducts(): Flow<List<ProductModel>>

    suspend fun countProductsFromCategoryId(categoryId: Int): Int

    suspend fun getPackagesIdFromProduct(productId: Int): List<ProductModel>

    suspend fun getProductsIdFromCategoryId(categoryId: Int): List<Int>

    suspend fun getNameById(id: Int): String?

    suspend fun verifyBarcode(barcode: String): Boolean
}