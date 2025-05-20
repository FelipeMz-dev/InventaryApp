package com.felipemz.inventaryapp.model

data class ProductRatingEntity(
    override val rating: Int,
    override val totalValue: Int = 0,
    val product: ProductEntity = ProductEntity(),
): BaseRatingEntity()