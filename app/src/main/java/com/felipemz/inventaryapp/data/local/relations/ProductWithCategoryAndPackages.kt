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
        parentColumn = "id",
        entityColumn = "packageId"
    )
    val packageProducts: List<ProductPackageEntity>
)