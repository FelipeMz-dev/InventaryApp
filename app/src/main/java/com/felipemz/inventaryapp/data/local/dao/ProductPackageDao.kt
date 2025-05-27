package com.felipemz.inventaryapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felipemz.inventaryapp.data.local.entity.ProductPackageEntity

@Dao
interface ProductPackageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(packages: List<ProductPackageEntity>)

    @Query("SELECT * FROM product_packages WHERE packageId = :packageId")
    suspend fun getCompositions(packageId: Int): List<ProductPackageEntity>

    @Query("DELETE FROM product_packages WHERE packageId = :packageId")
    suspend fun deletePackagesByPackageId(packageId: Int)
}