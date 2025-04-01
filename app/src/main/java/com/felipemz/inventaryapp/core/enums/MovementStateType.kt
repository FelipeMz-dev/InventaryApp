package com.felipemz.inventaryapp.core.enums

private const val MOVEMENT_SALE = "Venta"
private const val MOVEMENT_EXPENSE = "Gasto"

enum class MovementStateType(val title: String, val typeName: String, val typeAction: String? = null) {
    NEW_SALE("Venta nueva", MOVEMENT_SALE, "Registrar venta"),
    DONE_SALE("Venta realizada", MOVEMENT_SALE),
    PENDING_SALE("Venta pendiente", MOVEMENT_SALE, "Registrar venta"),
    EDIT_SALE("Editar venta", MOVEMENT_SALE, "Editar venta"),
    NEW_EXPENSE("Gasto nuevo", MOVEMENT_EXPENSE, "Registrar gasto"),
    DONE_EXPENSE("Gasto realizado", MOVEMENT_EXPENSE),
    PENDING_EXPENSE("Gasto pendiente", MOVEMENT_EXPENSE, "Registrar gasto"),
    EDIT_EXPENSE("Editar gasto", MOVEMENT_EXPENSE, "Editar gasto");

    fun isDone() = this == DONE_SALE || this == DONE_EXPENSE
}