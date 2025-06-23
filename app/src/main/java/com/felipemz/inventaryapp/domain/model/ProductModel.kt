package com.felipemz.inventaryapp.domain.model

import com.felipemz.inventaryapp.core.EMPTY_STRING
import com.felipemz.inventaryapp.core.enums.QuantityType

data class ProductModel(
    val id: Int = 0,
    val name: String = EMPTY_STRING,
    val price: Int = 0,
    val category: CategoryModel = CategoryModel(),
    val image: ProductTypeImage = ProductTypeImage.LetterImage(EMPTY_STRING),
    val description: String? = null,
    val cost: Int? = null,
    val barcode: String? = null,
    val quantityModel: ProductQuantityModel? = null,
    val packageProducts: List<ProductPackageModel>? = null,
)

data class ProductPackageModel(
    val productId: Int = 0,
    val quantity: Int = 0,
)

data class ProductQuantityModel(
    val type: QuantityType = QuantityType.UNIT,
    val quantity: Int = 0,
)