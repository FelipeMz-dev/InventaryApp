package com.felipemz.inventaryapp.ui.commons.header_product

import com.felipemz.inventaryapp.domain.model.CategoryModel

sealed interface HeaderProductEvent {

    data class OnChangeSearchText(val text: String?) : HeaderProductEvent

    data class OnFocusSearch(val isFocus: Boolean) : HeaderProductEvent

    data class OnChangeCategory(val category: CategoryModel?) : HeaderProductEvent
}