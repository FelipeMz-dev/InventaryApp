package com.felipemz.inventaryapp.core.models

import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import java.time.LocalDate

data class RangeDateModel(
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val title: String = endDate?.let {
        FormatDateUtil.getOfCustomDateRange(startDate, endDate)
    } ?: FormatDateUtil.getOfCustomDate(startDate)
)