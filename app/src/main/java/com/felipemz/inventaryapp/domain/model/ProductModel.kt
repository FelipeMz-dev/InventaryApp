package com.felipemz.inventaryapp.domain.model

import com.felipemz.inventaryapp.core.EMPTY_STRING
import com.felipemz.inventaryapp.core.enums.QuantityType

data class ProductModel(
    val id: Int = 0,
    val name: String = EMPTY_STRING,
    val price: Int = 0,
    val category: CategoryModel = CategoryModel(),
    val image: ProductTypeImage = ProductTypeImage.LetterImage(EMPTY_STRING),
    val description: String = EMPTY_STRING,
    val cost: Int = 0,
    val quantityModel: ProductQuantityModel? = null,
    val packageProducts: List<ProductPackageModel>? = null,
)

data class ProductPackageModel(
    val productId: Int = 0,
    val quantity: Int = 0,
)

data class ProductSelectionChart(
    val product: ProductModel? = null,
    val quantity: Int = 0,
    val price: Int = 0
)

fun ProductSelectionChart.toProductPackageModel() = ProductPackageModel(
    productId = product?.id ?: 0,
    quantity = quantity,
)

data class ProductQuantityModel(
    val type: QuantityType = QuantityType.UNIT,
    val quantity: Int = 0,
)