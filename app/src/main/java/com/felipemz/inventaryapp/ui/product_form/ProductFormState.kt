package com.felipemz.inventaryapp.ui.product_form

import com.felipemz.inventaryapp.core.base.State
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.ProductQuantityEntity
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

data class ProductFormState(
    val originalProduct: ProductEntity? = null,
    val productList: List<ProductEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val images: List<ProductTypeImage> = emptyList(),
    val name: String = String(),
    val price: Int = 0,
    val category: CategoryEntity? = null,
    val imageSelected: ProductTypeImage = ProductTypeImage.LetterImage(String()),
    val description: String = String(),
    val cost: Int = 0,
    val quantityType: QuantityType? = null,
    val quantity: Int = 0,
    val packageProduct: ProductQuantityEntity? = null,
    val compositionProducts: List<ProductQuantityEntity>? = null,
    val enableToSave: Boolean = false,
): State
