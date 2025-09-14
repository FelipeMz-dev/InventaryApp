package com.felipemz.inventaryapp.ui.commons.datepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.customs.CustomSelectableDates
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.charts.RangeDateChart
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import java.time.LocalDate

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun CustomMonthPicker(
    modifier: Modifier,
    selectableDates: CustomSelectableDates,
    dateSelected: MutableState<RangeDateChart?>
) {

    val currentDate by remember { derivedStateOf { LocalDate.now() } }
    val currentYear = remember { mutableIntStateOf(currentDate.year) }
    var isYearSelector by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        HeadLineYearPicker(
            modifier = Modifier.fillMaxWidth(),
            dateSelected = dateSelected.value
        ) { dateSelected.value = null }

        HorizontalDivider()

        ButtonsYearSelector(
            modifier = Modifier.fillMaxWidth(),
            currentYear = currentYear,
            yearsEnabled = selectableDates.getSelectableYears(),
            isShowArrows = !isYearSelector
        ) { isYearSelector = !isYearSelector }

        if (isYearSelector) YearSelector(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            selectableDates = selectableDates,
            yearSelected = currentYear.intValue,
            currentYear = currentDate.year
        ) {
            if (it != currentYear.intValue) {
                currentYear.intValue = it
                dateSelected.value = null
            }
        } else MonthsSelector(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            dateSelected = dateSelected,
            currentYear = currentYear,
            selectableDates = selectableDates,
            currentDate = currentDate
        )

        HorizontalDivider()
    }
}

@Composable
private fun HeadLineYearPicker(
    modifier: Modifier,
    dateSelected: RangeDateChart?,
    onClear: () -> Unit,
) {

    Column(modifier = modifier) {

        Text(
            modifier = Modifier.padding(12.dp),
            text = "Selecciona un mes",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )

        TextButtonUnderline(
            text = dateSelected?.title ?: "No se ha seleccionado un mes",
            isEnabled = !dateSelected.isNull()
        ) { onClear() }
    }
}

@Composable
private fun ButtonsYearSelector(
    modifier: Modifier,
    currentYear: MutableState<Int>,
    yearsEnabled: IntRange,
    isShowArrows: Boolean,
    onAction: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .padding(4.dp)
                .clip(CircleShape)
                .clickable { onAction() }
                .padding(horizontal = 8.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.padding(12.dp),
                text = "Año: ${currentYear.value}",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall
            )

            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }

        isShowArrows.ifTrue {
            Row(
                modifier = Modifier.padding(end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            yearsEnabled
                                .contains(currentYear.value - 1)
                                .ifTrue { currentYear.value-- }
                        }
                        .padding(8.dp),
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null
                )

                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            yearsEnabled
                                .contains(currentYear.value + 1)
                                .ifTrue { currentYear.value++ }
                        }
                        .padding(8.dp),
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}
