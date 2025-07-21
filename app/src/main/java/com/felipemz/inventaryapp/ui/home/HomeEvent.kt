package com.felipemz.inventaryapp.ui.home

import com.felipemz.inventaryapp.core.base.Event
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.ui.home.tabs.HomeTabs
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.charts.RangeDateChart
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductEvent

sealed interface HomeEvent : Event {

    object Init : HomeEvent

    object OnOpenCalendar : HomeEvent

    data class OnFAB(val tabSelected: HomeTabs) : HomeEvent

    data class OnMovementFilterSelected(val filter: MovementsFilterChip) : HomeEvent

    data class OnLabelSelected(val label: String) : HomeEvent

    data class OnMovementsInverted(val isInverted: Boolean) : HomeEvent

    data class OnProductOrderSelected(
        val orderBy: ProductsOrderBy,
        val isInverted: Boolean
    ) : HomeEvent

    data class OnReportsCustomFilterSelected(val filter: RangeDateChart) : HomeEvent

    data class OnReportsFilterSelected(val filter: ReportsFilterDate) : HomeEvent

    data class OnOpenProduct(val product: ProductModel) : HomeEvent

    data class OnSetNameFilterProducts(val name: String?) : HomeEvent

    data class OnSetCategoryFilterProducts(val category: CategoryModel?) : HomeEvent

    data class OnFocusSearch(val isFocus: Boolean) : HomeEvent
}