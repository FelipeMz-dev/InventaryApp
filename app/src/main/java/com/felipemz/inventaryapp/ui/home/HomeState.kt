package com.felipemz.inventaryapp.ui.home

import com.felipemz.inventaryapp.core.base.State
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.MovementItemEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.models.RangeDateModel

data class HomeState(
    val searchText: String = String(),
    val isSearchFocused: Boolean = false,
    val categories: List<CategoryEntity> = emptyList(),
    val categorySelected: CategoryEntity = CategoryEntity(),
    val products: List<ProductEntity> = emptyList(),
    val movements: List<MovementItemEntity> = emptyList(),
    val movementLabelList: List<String> = emptyList(),
    val currentDate: String = "Lun. 30 de diciembre",
    val movementFilterText: String = String(),
    val totalAmount: Int = 0,
    val movementFilterSelected: MovementsFilterChip = MovementsFilterChip.ALL,
    val movementLabelSelected: String? = null,
    val isMovementsInverted: Boolean = false,
    val isShowLabelPopup: Boolean = false,
    val isProductOrderPopup: Boolean = false,
    val isReportsCalendarPopup: Boolean = false,
    val productOrderSelected: ProductsOrderBy = ProductsOrderBy.CATEGORY,
    val isProductOrderInverted: Boolean = false,
    val reportsFilterChipSelected: ReportsFilterDate? = ReportsFilterDate.TODAY,
    val reportsCustomFilterSelected: RangeDateModel? = null
) : State