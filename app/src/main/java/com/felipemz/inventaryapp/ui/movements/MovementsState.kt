package com.felipemz.inventaryapp.ui.movements

import com.felipemz.inventaryapp.core.base.State
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.model.ProductQuantityModel
import com.felipemz.inventaryapp.core.enums.MovementStateType

data class MovementsState(
    val movementId: Int? = null,
    val movementState: MovementStateType = MovementStateType.NEW_SALE,
    val movementNumber: Int = 0,
    val movementDate: String = String(),
    val selectedProducts: List<ProductQuantityModel> = emptyList(),
    val productList: List<ProductModel> = emptyList(),
    val total: Int = 0,
    val subTotal: Int = 0,
    val discount: Int = 0,
    val calculatorId: Int = 0,
): State
