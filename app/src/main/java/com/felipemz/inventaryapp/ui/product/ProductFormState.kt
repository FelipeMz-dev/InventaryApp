package com.felipemz.inventaryapp.ui.product

import com.felipemz.inventaryapp.core.base.State
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

data class ProductFormState(
    val loading: Boolean = false,
    val error: String = String(),
    val success: Boolean = false,
    val isNewProduct : Boolean = false,
    val product: ProductEntity = ProductEntity(),
    val idProduct: Int? = null,
    val categories: List<CategoryEntity> = emptyList(),
    val images: List<ProductTypeImage> = emptyList(),
    val name: String = String(),
    val description: String = String(),
    val price: Int = 0,
    val category: CategoryEntity? = null,
    val imageSelected: ProductTypeImage = ProductTypeImage.LetterImage(String()),
    val quantityType: QuantityType? = null,
    val quantity: Int = 0,
): State
