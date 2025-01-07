package com.felipemz.inventaryapp.core.enums

import com.felipemz.inventaryapp.R

enum class ReportsType(
    val text: String,
    val icon: Int
) {
    UTILITY("Utilidad", R.drawable.ic_utility),
    SALES_REPORTS("Reportes de ventas", R.drawable.ic_sales),
    EXPENSES_REPORTS("Reportes de gastos", R.drawable.ic_expenses),
    PRODUCTS_RATING("Productos más vendidos", R.drawable.ic_rating_products),
    CATEGORIES_RATING("Categorias más vendidas", R.drawable.ic_category)
}