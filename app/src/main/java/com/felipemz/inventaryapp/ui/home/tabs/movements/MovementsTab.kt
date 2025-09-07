package com.felipemz.inventaryapp.ui.home.tabs.movements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.utils.CurrencyUtil
import com.felipemz.inventaryapp.domain.model.MovementModel
import com.felipemz.inventaryapp.ui.commons.FilterChipRow
import com.felipemz.inventaryapp.ui.home.HomeEvent
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnLabelChangeToShow
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnLabelSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnMovementFilterSelected
import com.felipemz.inventaryapp.ui.home.components.MovementLabelDialog
import kotlin.math.absoluteValue

@Composable
internal fun MovementsTab(
    chipSelected: MovementsFilterChip,
    labelSelected: String?,
    movements: List<MovementModel>,
    movementLabelList: List<String>,
    isShowLabelPopup: Boolean,
    eventHandler: (HomeEvent) -> Unit,
) {

    if (isShowLabelPopup) {
        MovementLabelDialog(
            labelList = movementLabelList,
            onDismiss = { eventHandler(OnLabelChangeToShow(false)) },
            onSelect = { eventHandler(OnLabelSelected(it)) }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            FilterChipCategories(
                modifier = Modifier.fillMaxWidth(),
                chipList = MovementsFilterChip.entries,
                chipSelected = chipSelected,
                labelSelected = labelSelected,
            ) { eventHandler(OnMovementFilterSelected(it)) }
        }

        items(
            items = movements,
            key = { it.id }
        ) { movement ->
            MovementItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(6.dp),
                movement = movement,
                movementColor = movement.type.color?.let {
                    colorResource(id = it)
                } ?: MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun HeaderMovements(
    modifier: Modifier,
    chipSelected: MovementsFilterChip,
    labelSelected: String?,
    date: String,
    total: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            text = AnnotatedString(
                text = "Total de ${
                    when (chipSelected) {
                        MovementsFilterChip.ALL -> "Movimientos"
                        MovementsFilterChip.LABEL -> labelSelected ?: "Sin etiqueta"
                        else -> chipSelected.text
                    }
                } hoy, "
            ).plus(
                AnnotatedString(
                    text = date,
                    spanStyle = SpanStyle(textDecoration = TextDecoration.Underline)
                )
            ),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        Text(
            modifier = Modifier
                .padding(end = 12.dp)
                .background(
                    color = colorResource(if (total < 0) R.color.orange else R.color.blue).copy(alpha = 0.4f),
                    shape = CircleShape
                )
                .padding(horizontal = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            text = CurrencyUtil.formatPrice(total.absoluteValue, isLess = total < 0),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FilterChipCategories(
    modifier: Modifier,
    chipList: List<MovementsFilterChip>,
    chipSelected: MovementsFilterChip,
    labelSelected: String?,
    onChipSelected: (MovementsFilterChip) -> Unit,
) = FilterChipRow(
    modifier = modifier,
    trailingIcon = {
        if (it == MovementsFilterChip.LABEL) {
            {
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = null
                )
            }
        } else null
    },
    text = { if (it == MovementsFilterChip.LABEL) labelSelected ?: it.text else it.text },
    chipList = chipList,
    chipSelected = chipSelected,
    onSelectChip = onChipSelected,
)