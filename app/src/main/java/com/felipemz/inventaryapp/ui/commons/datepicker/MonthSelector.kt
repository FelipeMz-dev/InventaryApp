package com.felipemz.inventaryapp.ui.commons.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.customs.CustomSelectableDates
import com.felipemz.inventaryapp.core.models.RangeDateModel
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun MonthsSelector(
    modifier: Modifier,
    dateSelected: MutableState<RangeDateModel?>,
    currentYear: MutableIntState,
    selectableDates: CustomSelectableDates,
    currentDate: LocalDate
) {

    val months by remember { derivedStateOf { Month.entries } }
    val currentMonth by remember { derivedStateOf { currentDate.month } }
    val monthSelected by remember(dateSelected.value) { derivedStateOf { dateSelected.value?.startDate?.month } }

    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        months.forEach { month ->

            val isSelected by remember((dateSelected.value to currentYear.intValue)) {
                derivedStateOf { month == monthSelected && currentYear.intValue == dateSelected.value?.startDate?.year }
            }
            val isEnable by remember(currentYear.intValue) {
                derivedStateOf { selectableDates.isSelectableMonth(month.value, currentYear.intValue) }
            }

            MonthItem(
                modifier = Modifier.weight(1f),
                isSelected = isSelected,
                currentMonth = currentMonth,
                month = month,
                currentYear = currentYear,
                currentDate = currentDate,
                isEnable = isEnable,
                dateSelected = dateSelected
            )
        }
    }
}

@Composable
private fun MonthItem(
    modifier: Modifier,
    isSelected: Boolean,
    currentMonth: Month?,
    month: Month,
    currentYear: MutableIntState,
    currentDate: LocalDate,
    isEnable: Boolean,
    dateSelected: MutableState<RangeDateModel?>
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
) {
    Text(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 1f.takeIf { isSelected } ?: 0f),
                shape = CircleShape
            )
            .then(
                if (currentMonth == month && currentYear.intValue == currentDate.year) Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) else Modifier
            )
            .clip(CircleShape)
            .clickable(enabled = isEnable) {
                val dateMonthOfYear = LocalDate.of(currentYear.intValue, month, 1)
                dateSelected.value = RangeDateModel(
                    title = FormatDateUtil.getOfCustomMonth(dateMonthOfYear),
                    startDate = dateMonthOfYear
                )
            }
            .padding(horizontal = 12.dp, vertical = 2.dp),
        text = month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        color = MaterialTheme.colorScheme.let {
            when {
                isSelected -> it.onPrimary
                isEnable -> it.onSurface
                else -> it.onSurface.copy(alpha = 0.4f)
            }
        },
    )
}