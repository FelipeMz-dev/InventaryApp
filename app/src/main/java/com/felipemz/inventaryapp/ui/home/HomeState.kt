package com.felipemz.inventaryapp.ui.home

import com.felipemz.inventaryapp.core.base.State
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.MovementItemEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip

data class HomeState(
    val categories: List<CategoryEntity> = emptyList(),
    val products: List<ProductEntity> = emptyList(),
    val movements: List<MovementItemEntity> = emptyList(),
    val movementLabelList: List<String> = emptyList(),
    val currentDate: String = "Lun. 30 de diciembre",
    val totalAmount: Int = 0,
    val movementFilterSelected: MovementsFilterChip = MovementsFilterChip.ALL,
    val movementLabelSelected: String? = null,
    val isMovementsInverted: Boolean = false,
    val isShowLabelPopup: Boolean = false,
) : State