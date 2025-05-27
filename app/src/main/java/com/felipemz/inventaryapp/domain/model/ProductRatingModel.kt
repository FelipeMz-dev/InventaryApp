package com.felipemz.inventaryapp.domain.model

data class ProductRatingModel(
    override val rating: Int,
    override val totalValue: Int = 0,
    val product: ProductModel = ProductModel(),
): BaseRatingModel()