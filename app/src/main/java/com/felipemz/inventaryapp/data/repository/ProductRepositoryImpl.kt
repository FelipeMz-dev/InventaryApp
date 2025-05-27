package com.felipemz.inventaryapp.data.repository

import com.felipemz.inventaryapp.data.cache.ProductsCache
import com.felipemz.inventaryapp.data.local.dao.ProductDao
import com.felipemz.inventaryapp.data.local.dao.ProductPackageDao
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.repository.CategoryRepository
import com.felipemz.inventaryapp.domain.repository.ProductRepository
import com.felipemz.inventaryapp.domain.repository.mapper.toEntity
import com.felipemz.inventaryapp.domain.repository.mapper.toPackageEntities
import com.felipemz.inventaryapp.domain.repository.mapper.toProductModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class ProductRepositoryImpl(
    private val productDao: ProductDao,
    private val packageDao: ProductPackageDao,
) : ProductRepository {

    private val productCache = ProductsCache
    private var canObserveProducts = true

    override fun observeAllProductModels(): Flow<List<ProductModel>> {
        if (canObserveProducts) initProductObservation()
        return productCache.products
    }

    private fun initProductObservation() {
        canObserveProducts = false
        productDao.observeAllWithCategoryAndPackages()
            .map { list -> list.map { it.toProductModel() } }
            .onEach { productCache.setProducts(it) }
            .launchIn(CoroutineScope(Dispatchers.IO))
    }

    override suspend fun insertOrUpdate(product: ProductModel) {
        val productId = productDao.insert(product.toEntity()).toInt()
        val packageEntities = product.copy(id = productId).toPackageEntities()
        packageDao.deletePackagesByPackageId(productId)
        if (packageEntities.isNotEmpty()) {
            packageDao.insertAll(packageEntities)
        }
    }

    override suspend fun getById(id: Int): ProductModel? {
        return productDao.getProductWithRelationsById(id)?.toProductModel()
    }

    override suspend fun deleteById(id: Int) {
        productDao.getById(id)?.let { productDao.delete(it) }
        packageDao.deletePackagesByPackageId(id)
    }

    override fun getAllProducts(): Flow<List<ProductModel>> = flow {
        emit(productDao.getAllWithCategoryAndPackages().map { it.toProductModel() })
    }

    override suspend fun countProductsFromCategoryId(categoryId: Int): Int {
        return productDao.countProductsFromCategoryId(categoryId)
    }

    override suspend fun getProductsIdFromCategoryId(categoryId: Int): List<Int> {
        return productDao.getProductsIdFromCategoryId(categoryId)
    }

    override suspend fun getNameById(id: Int): String? {
        return productDao.getById(id)?.name
    }
}