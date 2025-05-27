package com.felipemz.inventaryapp.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.felipemz.inventaryapp.data.local.entity.CategoryEntity
import com.felipemz.inventaryapp.data.local.entity.ProductEntity
import com.felipemz.inventaryapp.data.local.entity.ProductPackageEntity

data class ProductWithCategoryAndPackages(
    @Embedded val product: ProductEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity,

    @Relation(
        entity = ProductEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ProductPackageEntity::class,
            parentColumn = "packageId",
            entityColumn = "productId"
        )
    )
    val packageProducts: List<ProductPackageWithQuantity>
)