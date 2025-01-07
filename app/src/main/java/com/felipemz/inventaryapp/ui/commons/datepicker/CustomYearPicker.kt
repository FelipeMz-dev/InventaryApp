package com.felipemz.inventaryapp.ui.commons.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
internal fun CustomYearPicker(modifier: Modifier) {
    Column(modifier = modifier) {
        val years by remember { derivedStateOf { DatePickerDefaults.YearRange.toList() } }
        val currentYear by remember { derivedStateOf { LocalDate.now().year } }
        var yearSelected by remember { mutableStateOf<Int?>(null) }
        val scrollState = rememberScrollState(
            initial = (400.dp.value * 5).toInt()
        )
        val yearsEnabled = remember { derivedStateOf { 2024..2025 } }

        HorizontalDivider()

        FlowRow(
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            years.forEach { year ->
                val isSelected by remember(yearSelected) { derivedStateOf { year == yearSelected } }
                val isEnable by remember(yearsEnabled) { derivedStateOf { yearsEnabled.value.contains(year) } }
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
                        .clickable(enabled = isEnable) { yearSelected = year }
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

        HorizontalDivider()
    }
}