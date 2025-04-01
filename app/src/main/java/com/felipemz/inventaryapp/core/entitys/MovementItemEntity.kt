package com.felipemz.inventaryapp.core.entitys

import com.felipemz.inventaryapp.core.enums.MovementItemType

data class MovementItemEntity(
    val type: MovementItemType = MovementItemType.MOVEMENT_PENDING,
    val number: Int? = null,
    val date: String = String(),
    val time: String = String(),
    val amount: Int = 0,
    val labels: List<String> = emptyList(),
)