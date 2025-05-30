package com.felipemz.inventaryapp.ui.product_form

import com.felipemz.inventaryapp.core.base.State
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.model.ProductSelectionChart
import com.felipemz.inventaryapp.domain.model.ProductTypeImage
import com.felipemz.inventaryapp.ui.product_form.components.alert_dialog.AlertDialogProductFormType

data class ProductFormState(
    val isLoading: Boolean = false,
    val alertDialog: AlertDialogProductFormType? = null,
    val originalProduct: ProductModel? = null,
    val productList: List<ProductModel> = emptyList(),
    val categories: List<CategoryModel> = emptyList(),
    val images: List<ProductTypeImage> = listOf(
        ProductTypeImage.LetterImage(String()),
        ProductTypeImage.EmojiImage(String()),
        ProductTypeImage.PhatImage(String())
    ),
    val name: String = String(),
    val price: Int = 0,
    val category: CategoryModel? = null,
    val imageSelected: ProductTypeImage = ProductTypeImage.LetterImage(String()),
    val description: String = String(),
    val cost: Int = 0,
    val quantityType: QuantityType? = null,
    val quantity: Int = 0,
    val packageProducts: List<ProductSelectionChart>? = null,
    val enableToSave: Boolean = false,
    val categoryIdToChange: Int? = null,
) : State

data class CategoryUseChart(
    val usedId: Int,
    val usesName: String,
    val isChanged: Boolean = false,
)