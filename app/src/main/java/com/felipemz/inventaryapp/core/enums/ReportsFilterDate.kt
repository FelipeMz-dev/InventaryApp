package com.felipemz.inventaryapp.core.enums

import com.felipemz.inventaryapp.core.utils.FormatDateUtil

enum class ReportsFilterDate(val text: String) {
    TODAY("Hoy"),
    YESTERDAY("Ayer"),
    THIS_WEEK("Esta semana"),
    LAST_WEEK("La semana pasada"),
    THIS_MONTH("Este mes"),
    LAST_MONTH("El mes pasado"),
    THIS_YEAR("Este año");

    fun getDateRange() = FormatDateUtil.getOfFilterChip(this)
}