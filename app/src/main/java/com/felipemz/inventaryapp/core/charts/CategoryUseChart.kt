package com.felipemz.inventaryapp.core.charts

data class CategoryUseChart(
    val usedId: Int,
    val usesName: String,
    val isChanged: Boolean = false,
)