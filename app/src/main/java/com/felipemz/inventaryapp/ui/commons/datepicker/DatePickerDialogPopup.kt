package com.felipemz.inventaryapp.ui.commons.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.customs.CustomSelectableDates
import com.felipemz.inventaryapp.core.enums.CustomFilterDate
import com.felipemz.inventaryapp.core.extensions.ifNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.extensions.toLocalDate
import com.felipemz.inventaryapp.core.extensions.toLong
import com.felipemz.inventaryapp.core.models.RangeDateModel
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogPopup(
    dateType: CustomFilterDate = CustomFilterDate.DAY,
    onConfirm: (RangeDateModel) -> Unit,
    onDismiss: () -> Unit,
) {

    val selectedDate = remember { mutableStateOf<RangeDateModel?>(null) }
    val currentDate by remember { derivedStateOf { LocalDate.now() } }
    val selectableDates by remember {
        derivedStateOf {
            CustomSelectableDates(
                rangeSelectableYears = 2024..2025,
                rangeSelectableDates = currentDate.minusDays(50).toLong()..currentDate.toLong()
            )
        }
    }

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        dismissButton = {
            TextButtonUnderline(
                text = "Salir",
                enabled = true
            ) { onDismiss() }
        },
        confirmButton = {
            TextButtonUnderline(
                text = "Confirmar",
                enabled = !selectedDate.isNull()
            ) { selectedDate.value?.let { onConfirm(it) } }
        }
    ) {
        when (dateType) {
            CustomFilterDate.DAY -> {

                val datePickerState = rememberDatePickerState(selectableDates = selectableDates)

                DatePicker(
                    state = datePickerState,
                    title = null,
                    showModeToggle = false,
                )
            }

            CustomFilterDate.MONTH -> {
                CustomYearPicker(Modifier.fillMaxWidth())
            }

            CustomFilterDate.YEAR -> {
                CustomYearPicker(Modifier.fillMaxWidth())
            }

            CustomFilterDate.DATE_RANGE -> {

                val dateRangePickerState = rememberDateRangePickerState(selectableDates = selectableDates)

                DateRangePicker(
                    state = dateRangePickerState,
                    showModeToggle = false,
                    title = null,
                    headline = {
                        HeadLineRangePicker(
                            modifier = Modifier.fillMaxWidth(),
                            selectedDate = selectedDate,
                            dateRangePickerState = dateRangePickerState
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeadLineRangePicker(
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
        }
    }

    Column(modifier = Modifier.clickable(enabled = false) { }) {

        Text(
            modifier = Modifier.padding(8.dp),
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Desde:",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    modifier = Modifier
                        .background(
                            color = selectedStartDateMillis?.let {
                                MaterialTheme.colorScheme.primary
                            } ?: Color.Gray.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                        .clickable {
                            dateRangePickerState.setSelection(
                                startDateMillis = null,
                                endDateMillis = null
                            )
                        }
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    text = selectedStartDateMillis?.let {
                        FormatDateUtil.getOfCustomDate(it.toLocalDate())
                    } ?: "Selecciona una fecha de inicio",
                    fontWeight = FontWeight.Bold,
                    color = selectedStartDateMillis?.let {
                        MaterialTheme.colorScheme.onPrimary
                    } ?: Color.Gray,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hasta:",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    modifier = Modifier
                        .background(
                            color = selectedEndDateMillis?.let {
                                MaterialTheme.colorScheme.primary
                            } ?: Color.Gray.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                        .clickable {
                            dateRangePickerState.setSelection(
                                startDateMillis = dateRangePickerState.selectedStartDateMillis,
                                endDateMillis = null
                            )
                        }
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    text = selectedEndDateMillis?.let {
                        FormatDateUtil.getOfCustomDate(it.toLocalDate())
                    } ?: "Selecciona una fecha de fin",
                    fontWeight = FontWeight.Bold,
                    color = selectedStartDateMillis?.let {
                        MaterialTheme.colorScheme.onPrimary
                    } ?: Color.Gray,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}