package com.felipemz.inventaryapp.ui.home

import com.felipemz.inventaryapp.core.base.Event
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.enums.HomeTabs
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.models.RangeDateModel

sealed interface HomeEvent : Event {

    object OnHideLabelPopup : HomeEvent

    object OnOpenProductOrderPopup : HomeEvent

    object OpenReportsCalendarPopup : HomeEvent

    object OnCloseReportsCalendarPopup : HomeEvent

    object OnOpenCalendar : HomeEvent

    data class OnFocusSearch(val isFocus: Boolean) : HomeEvent

    data class OnFAB(val tabSelected: HomeTabs) : HomeEvent

    data class OnChangeSearchText(val text: String) : HomeEvent

    data class OnCategorySelected(val category: CategoryEntity) : HomeEvent

    data class OnMovementFilterSelected(val filter: MovementsFilterChip) : HomeEvent

    data class OnLabelSelected(val label: String) : HomeEvent

    data class OnMovementsInverted(val isInverted: Boolean) : HomeEvent

    data class OnProductOrderSelected(
        val orderBy: ProductsOrderBy,
        val isInverted: Boolean
    ) : HomeEvent

    data class OnReportsCustomFilterSelected(val filter: RangeDateModel) : HomeEvent

    data class OnReportsFilterSelected(val filter: ReportsFilterDate) : HomeEvent

    data class OnOpenProduct(val product: ProductEntity) : HomeEvent
}