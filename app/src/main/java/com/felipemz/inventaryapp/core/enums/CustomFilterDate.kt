package com.felipemz.inventaryapp.core.enums

import com.felipemz.inventaryapp.core.models.RangeDateModel
import com.felipemz.inventaryapp.core.utils.FormatDateUtil

enum class CustomFilterDate(val text: String) {
    DAY("Día"){
        override fun getDateRange(rangeDate: RangeDateModel) = FormatDateUtil.getOfCustomDate(rangeDate.startDate)
    },
    MONTH("Mes"){
        override fun getDateRange(rangeDate: RangeDateModel) = FormatDateUtil.getOfCustomMonth(rangeDate.startDate)
    },
    YEAR("Año"){
        override fun getDateRange(rangeDate: RangeDateModel) = FormatDateUtil.getOfCustomYear(rangeDate.startDate)
    },
    DATE_RANGE("Rango de fechas"){
        override fun getDateRange(rangeDate: RangeDateModel) = FormatDateUtil.getOfCustomDateRange(rangeDate.startDate, rangeDate.endDate)
    };

    abstract fun getDateRange(rangeDate: RangeDateModel): String
}