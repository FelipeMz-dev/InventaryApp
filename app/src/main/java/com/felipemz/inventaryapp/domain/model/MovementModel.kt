package com.felipemz.inventaryapp.domain.model

import com.felipemz.inventaryapp.core.enums.MovementItemType

data class MovementModel(
    val id: Int = 0,
    val type: MovementItemType = MovementItemType.MOVEMENT_PENDING,
    val number: Int? = null,
    val date: String = "",
    val time: String = "",
    val subTotal: Int = 0,
    val discount: Int = 0,
    val total: Int = 0,
    val labels: List<String> = emptyList(),
    val bill: List<BillItem> = emptyList(),
)

data class BillItem(
    val productId: Int? = null,
    val concept: String = "",
    val value: Int = 0,
    val quantity: Int = 0,
)