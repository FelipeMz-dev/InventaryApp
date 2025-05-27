package com.felipemz.inventaryapp.ui.product_form

import com.felipemz.inventaryapp.core.base.State
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.model.ProductQuantityModel
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

data class ProductFormState(
    val originalProduct: ProductModel? = null,
    val productList: List<ProductModel> = emptyList(),
    val categories: List<CategoryModel> = emptyList(),
    val images: List<ProductTypeImage> = emptyList(),
    val name: String = String(),
    val price: Int = 0,
    val category: CategoryModel? = null,
    val imageSelected: ProductTypeImage = ProductTypeImage.LetterImage(String()),
    val description: String = String(),
    val cost: Int = 0,
    val quantityType: QuantityType? = null,
    val quantity: Int = 0,
    val packageProduct: ProductQuantityModel? = null,
    val compositionProducts: List<ProductQuantityModel>? = null,
    val enableToSave: Boolean = false,
): State
