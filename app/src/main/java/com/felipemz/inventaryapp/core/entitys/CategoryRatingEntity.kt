package com.felipemz.inventaryapp.core.entitys

data class CategoryRatingEntity(
    override val rating: Int = 0,
    override val  totalValue: Int = 0,
    val category: CategoryEntity = CategoryEntity(),
): BaseRatingEntity()