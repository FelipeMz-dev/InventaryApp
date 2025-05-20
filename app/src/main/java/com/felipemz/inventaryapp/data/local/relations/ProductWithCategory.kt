package com.felipemz.inventaryapp.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.felipemz.inventaryapp.model.CategoryModel
import com.felipemz.inventaryapp.model.ProductModel

data class ProductWithCategory(
    @Embedded val product: ProductModel,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryModel
)