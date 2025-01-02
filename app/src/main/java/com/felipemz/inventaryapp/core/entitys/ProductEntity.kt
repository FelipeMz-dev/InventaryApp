package com.felipemz.inventaryapp.core.entitys

import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

data class ProductEntity(
    val id: Int = 0,
    val name: String = String(),
    val information: String = String(),
    val categoryColor: Int = 0,
    val quantity: Int? = null,
    val price: Int = 0,
    val image: ProductTypeImage = ProductTypeImage.LetterImage(String())
)