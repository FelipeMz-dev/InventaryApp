package com.felipemz.inventaryapp.ui.product_form

sealed interface ProductFormAction {

    data class ShowMessage(val message: String) : ProductFormAction

    data class OnCategoryChangeDone(val productId: Int) : ProductFormAction

    data class OnCreateFromBarcode(val barcode: String) : ProductFormAction
}