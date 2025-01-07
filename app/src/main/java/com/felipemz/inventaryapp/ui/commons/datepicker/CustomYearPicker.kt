package com.felipemz.inventaryapp.ui.commons.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.models.RangeDateModel
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import java.time.LocalDate

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun CustomYearPicker(
    modifier: Modifier,
    selectableDates: SelectableDates,
    dateSelected: MutableState<RangeDateModel?>
) {

    val currentYear by remember { derivedStateOf { LocalDate.now().year } }

    Column(modifier = modifier) {

        HeadLineYearPicker(
            modifier = Modifier.fillMaxWidth(),
            dateSelected = dateSelected.value
        ) { dateSelected.value = null }

        HorizontalDivider()

        YearSelector(
            modifier = Modifier
                .fillMaxWidth()
                .height(HEIGHT_ITEM_YEAR_PICKER.dp * 6),
            yearSelected = dateSelected.value?.startDate?.year,
            selectableDates = selectableDates,
            currentYear = currentYear
        ) { year ->
            val dateYear = LocalDate.of(year, 1, 1)
            dateSelected.value = RangeDateModel(
                title = FormatDateUtil.getOfCustomYear(dateYear),
                startDate = dateYear
            )
        }

        HorizontalDivider()
    }
}

@Composable
private fun HeadLineYearPicker(
    modifier: Modifier,
    dateSelected: RangeDateModel?,
    onClear: () -> Unit,
) {

    Column(modifier = modifier) {

        Text(
            modifier = Modifier.padding(12.dp),
            text = "Selecciona un año",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )

        TextButtonUnderline(
            text = dateSelected?.title ?: "No se ha seleccionado un año",
            enabled = !dateSelected.isNull()
        ) { onClear() }
    }
}