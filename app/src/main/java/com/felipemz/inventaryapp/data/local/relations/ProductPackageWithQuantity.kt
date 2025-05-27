package com.felipemz.inventaryapp.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.felipemz.inventaryapp.data.local.entity.ProductEntity
import com.felipemz.inventaryapp.data.local.entity.ProductPackageEntity

data class ProductPackageWithQuantity(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId",
        entity = ProductPackageEntity::class
    )
    val packages: List<ProductPackageEntity>
)