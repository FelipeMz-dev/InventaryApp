package com.felipemz.inventaryapp.ui.home.tabs

import com.felipemz.inventaryapp.R

enum class HomeTabs(
    val tittle: String,
    val icon: Int
) {
    PRODUCTS("Productos", R.drawable.ic_products_tab),
    MOVEMENTS("Movimientos", R.drawable.ic_movements_tab),
    REPORTS("Reportes", R.drawable.ic_reports_tab),;
}