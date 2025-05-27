package com.felipemz.inventaryapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.felipemz.inventaryapp.data.local.entity.ProductEntity
import com.felipemz.inventaryapp.data.local.relations.ProductWithCategoryAndPackages
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity): Long

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: Int): ProductEntity?

    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<ProductEntity>>

    @Delete
    suspend fun delete(product: ProductEntity)

    @Transaction
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductWithRelationsById(id: Int): ProductWithCategoryAndPackages?

    @Transaction
    @Query("SELECT * FROM products")
    suspend fun getAllWithCategoryAndPackages(): List<ProductWithCategoryAndPackages>

    @Transaction
    @Query("SELECT * FROM products")
    fun observeAllWithCategoryAndPackages(): Flow<List<ProductWithCategoryAndPackages>>

    @Query("SELECT COUNT(*) FROM products WHERE categoryId = :categoryId")
    suspend fun countProductsFromCategoryId(categoryId: Int): Int

    @Query("SELECT id FROM products WHERE categoryId = :categoryId")
    suspend fun getProductsIdFromCategoryId(categoryId: Int): List<Int>

}