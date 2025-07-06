package com.felipemz.inventaryapp.core.charts

import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import java.time.LocalDate

data class RangeDateChart(
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val title: String = endDate?.let {
        FormatDateUtil.getOfCustomDateRange(startDate, endDate)
    } ?: FormatDateUtil.getOfCustomDate(startDate)
)