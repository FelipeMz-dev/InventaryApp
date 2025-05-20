package com.felipemz.inventaryapp.model

import com.felipemz.inventaryapp.ui.home.tabs.products.ProductQuantityChart
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

data class ProductEntity(
    val id: Int = 0,
    val name: String = String(),
    val price: Int = 0,
    val category: CategoryEntity = CategoryEntity(),
    val image: ProductTypeImage = ProductTypeImage.LetterImage(String()),
    val description: String = String(),
    val cost: Int = 0,
    val quantityChart: ProductQuantityChart? = null,
    val packageProduct: ProductSelectionEntity? = null,
    val compositionProducts: List<ProductSelectionEntity>? = null,
)

data class ProductSelectionEntity(
    val id: Int = 0,
    val quantity: Int = 0,
)

data class ProductQuantityEntity(
    val product: ProductEntity? = null,
    val quantity: Int = 0,
    val price: Int = 0
)

fun ProductQuantityEntity.toProductSelectionEntity() = ProductSelectionEntity(
    id = product?.id ?: 0,
    quantity = quantity,
)
