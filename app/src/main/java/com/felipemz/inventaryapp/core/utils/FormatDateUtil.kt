package com.felipemz.inventaryapp.core.utils

import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object FormatDateUtil {

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun getOfFilterChip(filterDate: ReportsFilterDate) = when (filterDate) {
        ReportsFilterDate.TODAY -> {
            val today = LocalDate.now()
            val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val month = today.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            "${filterDate.text} $dayOfWeek ${today.dayOfMonth} de $month del ${today.year}"
        }
        ReportsFilterDate.YESTERDAY -> {
            val yesterday = LocalDate.now().minusDays(1)
            val dayOfWeek = yesterday.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val month = yesterday.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            "${filterDate.text} $dayOfWeek ${yesterday.dayOfMonth} de $month del ${yesterday.year}"
        }
        ReportsFilterDate.THIS_WEEK -> {
            val today = LocalDate.now()
            val startOfWeek = today.with(DayOfWeek.MONDAY)
            val startMonth = startOfWeek.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val todayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val dayOfWeek = startOfWeek.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val month = startOfWeek.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            "${filterDate.text} desde el $dayOfWeek ${startOfWeek.dayOfMonth} de $month del ${startOfWeek.year} " +
                    "hasta hoy $todayOfWeek ${today.dayOfMonth} de $startMonth del ${today.year}"
        }
        ReportsFilterDate.LAST_WEEK -> {
            val today = LocalDate.now()
            val startOfLastWeek = today.with(DayOfWeek.MONDAY).minusWeeks(1)
            val startMonth = startOfLastWeek.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val endOfLastWeek = startOfLastWeek.plusDays(6)
            val dayOfWeek = startOfLastWeek.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val endDayOfWeek = endOfLastWeek.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val endMonth = endOfLastWeek.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            "${filterDate.text} desde el $dayOfWeek ${startOfLastWeek.dayOfMonth} de $startMonth del ${startOfLastWeek.year} " +
                    "hasta el $endDayOfWeek ${endOfLastWeek.dayOfMonth} de $endMonth del ${endOfLastWeek.year}"
        }
        ReportsFilterDate.THIS_MONTH -> {
            val today = LocalDate.now()
            val startOfMonth = today.withDayOfMonth(1)
            val month = startOfMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val year = startOfMonth.year
            "${filterDate.text} $month del $year"
        }
        ReportsFilterDate.LAST_MONTH -> {
            val today = LocalDate.now()
            val startOfLastMonth = today.minusMonths(1).withDayOfMonth(1)
            val endOfLastMonth = startOfLastMonth.plusMonths(1).minusDays(1)
            val month = startOfLastMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val year = startOfLastMonth.year
            "${filterDate.text} $month $year"
        }
        ReportsFilterDate.THIS_YEAR -> {
            val today = LocalDate.now()
            val startOfYear = today.withDayOfYear(1)
            "${filterDate.text} ${startOfYear.year}"
        }
    }

    fun getOfCustomDate(date: LocalDate): String {
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val month = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return "El día $dayOfWeek ${date.dayOfMonth} de $month del ${date.year}"
    }

    fun getOfCustomMonth(month: LocalDate): String {
        val monthName = month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val year = month.year
        return "El mes $monthName del $year"
    }

    fun getOfCustomYear(year: LocalDate): String {
        val yearNumber = year.year
        return "El año $yearNumber"
    }

    fun getOfCustomDateRange(
        start: LocalDate,
        end: LocalDate?
    ): String {
        val startDayOfWeek = start.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val startMonth = start.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val endDayOfWeek = end?.dayOfWeek?.getDisplayName(TextStyle.FULL, Locale.getDefault())
            ?: LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val endMonth = end?.month?.getDisplayName(TextStyle.FULL, Locale.getDefault())
            ?: LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return "Desde el $startDayOfWeek ${start.dayOfMonth} de $startMonth del ${start.year} " +
                "hasta el $endDayOfWeek ${end?.dayOfMonth} de $endMonth del ${end?.year}"
    }
}