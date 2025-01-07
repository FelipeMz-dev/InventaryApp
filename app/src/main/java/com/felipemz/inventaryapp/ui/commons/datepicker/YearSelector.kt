package com.felipemz.inventaryapp.ui.commons.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

internal const val HEIGHT_ITEM_YEAR_PICKER = 50

@Composable
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
internal fun YearSelector(
    modifier: Modifier,
    yearSelected: Int?,
    selectableDates: SelectableDates,
    currentYear: Int,
    onYearSelected: (Int) -> Unit = {}
) {

    val years by remember { derivedStateOf { DatePickerDefaults.YearRange.toList() } }
    val scrollState = rememberScrollState(initial = (HEIGHT_ITEM_YEAR_PICKER.dp.value * (years.size / 3)).toInt())

    FlowRow(
        modifier = modifier.verticalScroll(scrollState),
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        years.forEach { year ->

            val isSelected by remember(yearSelected) { derivedStateOf { year == yearSelected } }
            val isEnable by remember { derivedStateOf { selectableDates.isSelectableYear(year) } }

            Text(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 1f.takeIf { isSelected } ?: 0f),
                        shape = CircleShape
                    )
                    .then(
                        if (year == currentYear) Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ) else Modifier
                    )
                    .clip(CircleShape)
                    .clickable(enabled = isEnable) { onYearSelected(year) }
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                text = year.toString(),
                color = MaterialTheme.colorScheme.let {
                    when {
                        isSelected -> it.onPrimary
                        isEnable -> it.onSurface
                        else -> it.onSurface.copy(alpha = 0.4f)
                    }
                },
            )
        }
    }
}