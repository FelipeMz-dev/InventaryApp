package com.felipemz.inventaryapp.ui.movements

import com.felipemz.inventaryapp.core.base.State
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.ProductQuantityEntity
import com.felipemz.inventaryapp.core.enums.MovementStateType

data class MovementsState(
    val movementId: Int? = null,
    val movementState: MovementStateType = MovementStateType.NEW_SALE,
    val movementNumber: Int = 0,
    val movementDate: String = String(),
    val selectedProducts: List<ProductQuantityEntity> = emptyList(),
    val productList: List<ProductEntity> = emptyList(),
    val total: Int = 0,
    val subTotal: Int = 0,
    val discount: Int = 0,
    val calculatorId: Int = 0,
): State
