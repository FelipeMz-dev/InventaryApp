package com.felipemz.inventaryapp.ui.commons.datepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.extensions.ifNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.extensions.toLocalDate
import com.felipemz.inventaryapp.core.models.RangeDateModel
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HeadLineRangePicker(
    modifier: Modifier,
    selectedDate: MutableState<RangeDateModel?>,
    dateRangePickerState: DateRangePickerState,
) {

    val selectedStartDateMillis by remember(dateRangePickerState) {
        derivedStateOf { dateRangePickerState.selectedStartDateMillis }
    }
    val selectedEndDateMillis by remember(dateRangePickerState) {
        derivedStateOf { dateRangePickerState.selectedEndDateMillis }
    }

    LaunchedEffect((selectedStartDateMillis to selectedEndDateMillis)) {
        selectedEndDateMillis.ifNotNull { end ->
            selectedStartDateMillis.ifNotNull { start ->
                selectedDate.value = RangeDateModel(
                    startDate = start.toLocalDate(),
                    endDate = end.toLocalDate()
                )
            }
        } ?: run { selectedDate.value = null }
    }

    Column(modifier = modifier) {

        Text(
            modifier = Modifier.padding(12.dp),
            text = "Selecciona un rango de fechas",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DateSelection(
                modifier = modifier.weight(1f),
                title = "Desde:",
                placeHolder = "Selecciona una fecha de inicio",
                selectedDateMillis = selectedStartDateMillis,
            ) {
                dateRangePickerState.setSelection(
                    startDateMillis = null,
                    endDateMillis = null
                )
            }

            DateSelection(
                modifier = modifier.weight(1f),
                title = "Hasta:",
                placeHolder = "Selecciona una fecha de fin",
                selectedDateMillis = selectedEndDateMillis,
            ) {
                dateRangePickerState.setSelection(
                    startDateMillis = selectedStartDateMillis,
                    endDateMillis = null
                )
            }
        }
    }
}

@Composable
private fun DateSelection(
    modifier: Modifier,
    title: String,
    placeHolder: String,
    selectedDateMillis: Long?,
    onAction: () -> Unit = {},
) {
    Column(modifier = modifier) {

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )

        TextButtonUnderline(
            text = selectedDateMillis?.let {
                FormatDateUtil.getOfCustomDate(it.toLocalDate())
            } ?: placeHolder,
            enabled = !selectedDateMillis.isNull()
        ) { onAction() }
    }
}