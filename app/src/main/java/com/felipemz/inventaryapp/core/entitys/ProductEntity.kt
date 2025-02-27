package com.felipemz.inventaryapp.core.entitys

import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

data class ProductEntity(
    val id: Int = 0,
    val name: String = String(),
    val price: Int = 0,
    val category: CategoryEntity = CategoryEntity(),
    val image: ProductTypeImage = ProductTypeImage.LetterImage(String()),
    val description: String = String(),
    val cost: Int = 0,
    val quantityType: QuantityType? = null,
    val quantity: Int? = null,
    val packageProduct: ProductQuantityEntity? = null,
    val compositionProducts: List<ProductQuantityEntity>? = null,
)

data class ProductSelectionEntity(
    val id: Int = 0,
    val quantity: Int = 0,
)

data class ProductQuantityEntity(
    val product: ProductEntity? = null,
    val quantity: Int = 0,
)
