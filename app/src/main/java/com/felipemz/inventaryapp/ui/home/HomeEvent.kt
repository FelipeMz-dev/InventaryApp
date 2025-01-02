package com.felipemz.inventaryapp.ui.home

import com.felipemz.inventaryapp.core.base.Event
import com.felipemz.inventaryapp.core.enums.HomeTabs
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip

sealed interface HomeEvent : Event {

    object OnCategorySelected : HomeEvent

    object OnHideLabelPopup : HomeEvent

    data class OnFAB(val tabSelected: HomeTabs): HomeEvent

    data class OnMovementFilterSelected(val filter: MovementsFilterChip): HomeEvent

    data class OnLabelSelected(val label: String): HomeEvent

    data class OnMovementsInverted(val isInverted: Boolean) : HomeEvent
}