package com.felipemz.inventaryapp.core.enums

import com.felipemz.inventaryapp.R

enum class MovementType(
    val text: String,
    val icon: Int,
    val color: Int?,
    val scaleY: Float,
) {
    MOVEMENT_SALE("Venta", R.drawable.ic_movement_up, R.color.blue, 1f),
    MOVEMENT_EXPENSE("Gasto", R.drawable.ic_movement_up, R.color.orange, -1f),
    MOVEMENT_PENDING("Pendiente", R.drawable.ic_movement_pending, null, 1f),
}