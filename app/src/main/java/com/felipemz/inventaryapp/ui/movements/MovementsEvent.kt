package com.felipemz.inventaryapp.ui.movements

import com.felipemz.inventaryapp.core.base.Event
import com.felipemz.inventaryapp.model.ProductQuantityEntity

sealed interface MovementsEvent : Event {

    data object OnBack : MovementsEvent

    data object OnSaveMovement : MovementsEvent

    data object OnDeleteMovement : MovementsEvent

    data object IncrementCalculatorId : MovementsEvent

    data class OnSelectProduct(val product: ProductQuantityEntity) : MovementsEvent

    data class OnChangeDiscount(val discount: Int) : MovementsEvent
}
