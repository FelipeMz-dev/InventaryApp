package com.felipemz.inventaryapp.data.repository

import com.felipemz.inventaryapp.data.local.dao.ProductDao
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val dao: ProductDao
) : ProductRepository {

    override fun getAllProducts(): Flow<List<ProductModel>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getProductById(id: Int): ProductModel? =
        dao.getById(id)?.toDomain()

    override suspend fun insertProduct(product: ProductModel) {
        dao.insert(product.toEntity())
    }
}