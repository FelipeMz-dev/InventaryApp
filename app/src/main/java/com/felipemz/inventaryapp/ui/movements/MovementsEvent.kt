package com.felipemz.inventaryapp.ui.movements

import com.felipemz.inventaryapp.core.base.Event
import com.felipemz.inventaryapp.ui.commons.actions.BillActions

sealed interface MovementsEvent : Event {

    data object Init : MovementsEvent

    data object OnBack : MovementsEvent

    data object OnSaveMovement : MovementsEvent

    data object OnDeleteMovement : MovementsEvent

    data object OnClearBarcodeError : MovementsEvent

    data class OnSelectProductFromBarcode(val barcode: String) : MovementsEvent

    data class OnChangeDiscount(val discount: Int) : MovementsEvent

    data class OnExecuteAction(val action: MovementsAction) : MovementsEvent

    data class OnInvoiceAction(val action: BillActions) : MovementsEvent
}
