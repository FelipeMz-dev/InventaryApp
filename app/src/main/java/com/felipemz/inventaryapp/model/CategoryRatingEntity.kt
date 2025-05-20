package com.felipemz.inventaryapp.model

data class CategoryRatingEntity(
    override val rating: Int = 0,
    override val  totalValue: Int = 0,
    val category: CategoryEntity = CategoryEntity(),
): BaseRatingEntity()