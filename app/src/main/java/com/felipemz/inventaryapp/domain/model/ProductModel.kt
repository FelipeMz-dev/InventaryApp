package com.felipemz.inventaryapp.domain.model

import com.felipemz.inventaryapp.core.EMPTY_STRING

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