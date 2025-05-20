package com.felipemz.inventaryapp.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.ProductModel

data class ProductWithCategory(
    @Embedded val product: ProductModel,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryModel
)