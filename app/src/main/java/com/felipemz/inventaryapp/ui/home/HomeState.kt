package com.felipemz.inventaryapp.ui.home

import com.felipemz.inventaryapp.core.base.State
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.MovementModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.charts.RangeDateChart

data class HomeState(
    val searchText: String = String(),
    val isSearchFocused: Boolean = false,
    val categories: List<CategoryModel> = emptyList(),
    val categorySelected: CategoryModel? = null,
    val products: List<ProductModel> = emptyList(),
    val movements: List<MovementModel> = emptyList(),
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
    val productOrderSelected: ProductsOrderBy = ProductsOrderBy.ID,
    val isProductOrderInverted: Boolean = false,
    val reportsFilterChipSelected: ReportsFilterDate? = ReportsFilterDate.TODAY,
    val reportsCustomFilterSelected: RangeDateChart? = null
) : State