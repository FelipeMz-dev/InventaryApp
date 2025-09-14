package com.felipemz.inventaryapp.ui.commons.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerState
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
import com.felipemz.inventaryapp.core.charts.RangeDateChart
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HeadLineDatePicker(
    modifier: Modifier,
    selectedDate: MutableState<RangeDateChart?>,
    datePickerState: DatePickerState,
) {

    val selectedDateMillis by remember(datePickerState) {
        derivedStateOf { datePickerState.selectedDateMillis }
    }

    LaunchedEffect(selectedDateMillis) {
        selectedDateMillis.ifNotNull {
            selectedDate.value = RangeDateChart(it.toLocalDate())
        } ?: run { selectedDate.value = null }
    }

    Column(modifier = modifier) {

        Text(
            modifier = Modifier.padding(12.dp),
            text = "Selecciona una fecha",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )

        TextButtonUnderline(
            text = selectedDateMillis?.let {
                FormatDateUtil.getOfCustomDate(it.toLocalDate())
            } ?: "No se ha seleccionado una fecha",
            isEnabled = !selectedDateMillis.isNull()
        ) { datePickerState.selectedDateMillis = null }
    }
}