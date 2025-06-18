package com.felipemz.inventaryapp.domain.model

data class CategoryRatingModel(
    override val rating: Int = 0,
    override val  totalValue: Int = 0,
    val category: CategoryModel = CategoryModel(),
): BaseRatingModel()