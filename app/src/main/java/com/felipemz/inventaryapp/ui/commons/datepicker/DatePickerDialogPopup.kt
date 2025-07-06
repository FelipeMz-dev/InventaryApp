package com.felipemz.inventaryapp.ui.commons.datepicker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.felipemz.inventaryapp.core.customs.CustomSelectableDates
import com.felipemz.inventaryapp.core.enums.CustomFilterDate
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.extensions.toLong
import com.felipemz.inventaryapp.core.charts.RangeDateChart
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogPopup(
    dateType: CustomFilterDate = CustomFilterDate.DAY,
    onConfirm: (RangeDateChart) -> Unit,
    onDismiss: () -> Unit,
) {

    val selectedDate = remember { mutableStateOf<RangeDateChart?>(null) }
    val currentDate by remember { derivedStateOf { LocalDate.now() } }
    val selectableDates by remember {
        derivedStateOf {
            CustomSelectableDates(
                rangeSelectableDates = currentDate.minusDays(50).toLong()..currentDate.toLong()
            )
        }
    }

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        dismissButton = {
            TextButtonUnderline(
                text = "Salir",
                isEnabled = true
            ) { onDismiss() }
        },
        confirmButton = {
            TextButtonUnderline(
                text = "Confirmar",
                isEnabled = !selectedDate.value.isNull()
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
                    headline = {
                        HeadLineDatePicker(
                            modifier = Modifier.fillMaxWidth(),
                            selectedDate = selectedDate,
                            datePickerState = datePickerState
                        )
                    }
                )
            }

            CustomFilterDate.MONTH -> {
                CustomMonthPicker(
                    Modifier.fillMaxWidth(),
                    selectableDates = selectableDates,
                    dateSelected = selectedDate
                )
            }

            CustomFilterDate.YEAR -> {
                CustomYearPicker(
                    Modifier.fillMaxWidth(),
                    selectableDates = selectableDates,
                    dateSelected = selectedDate
                )
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