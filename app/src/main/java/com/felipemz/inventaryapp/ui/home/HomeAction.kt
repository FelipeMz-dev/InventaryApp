package com.felipemz.inventaryapp.ui.home

sealed interface HomeAction {
    data class ShowMessage(val message: String) : HomeAction

    data class OpenProduct(val productId: Int) : HomeAction

    data class CreateProductFromBarcode(val barcode: String) : HomeAction

    data object OnBack : HomeAction
}