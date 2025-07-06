package com.felipemz.inventaryapp.ui.commons.actions

import com.felipemz.inventaryapp.core.charts.BillItemChart

sealed interface BillActions {

    data class OnInsertItem(val item: BillItemChart) : BillActions

    data class OnUpdateItem(val item: BillItemChart) : BillActions

    data class OnRemoveItem(val item: BillItemChart) : BillActions

    data class OnAddItem(val item: BillItemChart) : BillActions

    data class OnSubtractItem(val item: BillItemChart) : BillActions
}