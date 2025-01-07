package com.felipemz.inventaryapp.ui.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.CustomFilterDate
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.models.RangeDateModel
import com.felipemz.inventaryapp.ui.commons.HorizontalDotDivider
import com.felipemz.inventaryapp.ui.commons.PopupDialog
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import com.felipemz.inventaryapp.ui.commons.datepicker.DatePickerDialogPopup

@Composable
fun ReportsCalendarPopup(
    onAccept: (RangeDateModel) -> Unit,
    onClose: () -> Unit
) {

    var filterTypeSelected by remember { mutableStateOf(CustomFilterDate.DAY) }
    var dateSelected by remember { mutableStateOf<RangeDateModel?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    PopupDialog(
        title = stringResource(R.string.copy_search_date),
        onClose = { onClose() }
    ) {

        TopFilterTypeSelector(
            modifier = Modifier.fillMaxWidth(),
            filterTypeSelected = filterTypeSelected,
        ) {
            filterTypeSelected = it
            dateSelected = null
        }

        HorizontalDotDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        BodySearchDate(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            dateSelected = dateSelected,
            filterTypeSelected = filterTypeSelected
        ) { showDatePicker = true }

        HorizontalDotDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextButtonUnderline(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.copy_accept),
            enabled = !dateSelected.isNull()
        ) {
            dateSelected?.let { onAccept(it) }
        }
    }

    if (showDatePicker) {
        DatePickerDialogPopup(
            dateType = filterTypeSelected,
            onDismiss = { showDatePicker = false },
            onConfirm = {
                showDatePicker = false
                dateSelected = it
            }
        )
    }
}

@Composable
private fun TopFilterTypeSelector(
    modifier: Modifier,
    filterTypeSelected: CustomFilterDate,
    onSelect: (CustomFilterDate) -> Unit,
) {

    var showFullList by remember { mutableStateOf(false) }

    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showFullList = !showFullList },
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {

            Text(
                text = stringResource(R.string.copy_search_by, filterTypeSelected.text),
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }

    AnimatedVisibility(visible = showFullList) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            CustomFilterDate.entries.filterNot { it == filterTypeSelected }.forEach { filter ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.6f)
                        .clickable {
                            onSelect(filter)
                            showFullList = false
                        },
                    text = filter.text,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BodySearchDate(
    modifier: Modifier,
    dateSelected: RangeDateModel?,
    filterTypeSelected: CustomFilterDate,
    onClick: () -> Unit,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
) {

    Text(
        modifier = Modifier,
        text = dateSelected?.title ?: "No hay fecha seleccionada",
        style = if (filterTypeSelected == CustomFilterDate.DATE_RANGE && dateSelected != null) {
            MaterialTheme.typography.labelMedium
        } else MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface.let {
            it.copy(alpha = dateSelected?.let { 1f } ?: 0.6f)
        },
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )

    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(id = R.string.copy_search),
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Black,
        )

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}