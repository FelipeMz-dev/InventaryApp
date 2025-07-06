package com.felipemz.inventaryapp.core.charts

import com.felipemz.inventaryapp.domain.model.ProductModel

data class BillItemChart(
    val product: ProductModel? = null,
    val concept: String = "",
    val value: Int = 0,
    val quantity: Int = 1,
)