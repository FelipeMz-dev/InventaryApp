package com.felipemz.inventaryapp.core.customs

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.toLocalDate
import com.felipemz.inventaryapp.core.extensions.toLong
import java.time.LocalDate
import java.time.Year

@OptIn(ExperimentalMaterial3Api::class)
class CustomSelectableDates(private val rangeSelectableDates: LongRange) : SelectableDates {

    private val rangeSelectableYears = rangeSelectableDates.first.toLocalDate().year..rangeSelectableDates.last.toLocalDate().year

    fun getSelectableYears(): IntRange {
        return rangeSelectableYears
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year in rangeSelectableYears
    }

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis in rangeSelectableDates
    }

    fun isSelectableMonth(
        month: Int,
        year: Int
    ): Boolean {
        if (year !in rangeSelectableYears) return false
        val currentMonth = LocalDate.of(year, month, 1)
        val isYearLeap = Year.of(year).isLeap
        val lastDayOfMonth = currentMonth.month.length(isYearLeap)
        return (1..lastDayOfMonth).any {
            isSelectableDate(currentMonth.withDayOfMonth(it).toLong())
        }
    }
}