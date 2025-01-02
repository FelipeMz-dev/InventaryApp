package com.felipemz.inventaryapp.ui.home

import com.felipemz.inventaryapp.core.base.Event
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.enums.HomeTabs
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy

sealed interface HomeEvent : Event {

    object OnHideLabelPopup : HomeEvent

    object OnOpenProductOrderPopup : HomeEvent

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
}