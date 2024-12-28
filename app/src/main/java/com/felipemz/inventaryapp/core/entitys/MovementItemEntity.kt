package com.felipemz.inventaryapp.core.entitys

import com.felipemz.inventaryapp.core.enums.MovementType

data class MovementItemEntity(
    val type: MovementType = MovementType.MOVEMENT_PENDING,
    val number: Int? = null,
    val date: String = String(),
    val time: String = String(),
    val amount: Int = 0,
    val labels: List<String> = emptyList(),
)