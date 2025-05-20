package com.felipemz.inventaryapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felipemz.inventaryapp.data.local.entity.ProductCompositionEntity

@Dao
interface ProductCompositionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(composition: ProductCompositionEntity)

    @Query("SELECT * FROM product_compositions WHERE packageId = :packageId")
    suspend fun getCompositions(packageId: Int): List<ProductCompositionEntity>
}