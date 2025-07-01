package com.felipemz.inventaryapp.ui.movements

sealed interface MovementsAction {

    data class ShowMessage(val message: String) : MovementsAction

    data class CreateProductFromBarcode(val barcode: String) : MovementsAction

    data class OnChangeDiscount(val discount: Int) : MovementsAction

    data object OnBack : MovementsAction

    data object OnSaveMovement : MovementsAction

    data object OnDeleteMovement : MovementsAction
}