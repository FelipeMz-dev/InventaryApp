package com.felipemz.inventaryapp.domain.model

import com.felipemz.inventaryapp.ui.home.tabs.products.ProductQuantityChart
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

data class ProductModel(
    val id: Int = 0,
    val name: String = String(),
    val price: Int = 0,
    val category: CategoryModel = CategoryModel(),
    val image: ProductTypeImage = ProductTypeImage.LetterImage(String()),
    val description: String = String(),
    val cost: Int = 0,
    val quantityChart: ProductQuantityChart? = null,
    val packageProduct: ProductSelectionModel? = null,
    val compositionProducts: List<ProductSelectionModel>? = null,
)

data class ProductSelectionModel(
    val id: Int = 0,
    val quantity: Int = 0,
)

data class ProductQuantityModel(
    val product: ProductModel? = null,
    val quantity: Int = 0,
    val price: Int = 0
)

fun ProductQuantityModel.toProductSelectionEntity() = ProductSelectionModel(
    id = product?.id ?: 0,
    quantity = quantity,
)
