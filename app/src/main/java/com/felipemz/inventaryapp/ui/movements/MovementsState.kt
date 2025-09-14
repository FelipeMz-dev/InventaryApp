package com.felipemz.inventaryapp.ui.movements

import com.felipemz.inventaryapp.core.base.State
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.core.enums.MovementStateType
import com.felipemz.inventaryapp.domain.model.CategoryModel

data class MovementsState(
    val movementId: Int? = null,
    val movementState: MovementStateType = MovementStateType.NEW_SALE,
    val movementNumber: Int = 0,
    val movementDate: String = String(),
    val billList: List<BillItemChart> = emptyList(),
    val categories: List<CategoryModel> = emptyList(),
    val searchText: String = String(),
    val total: Int = 0,
    val subTotal: Int = 0,
    val discount: Int = 0,
    val errorBarcode: String? = null,
): State