package com.felipemz.inventaryapp.ui.home.tabs.movements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.entitys.MovementItemEntity
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.handler.PriceHandler
import com.felipemz.inventaryapp.ui.home.HomeEvent
import kotlin.math.absoluteValue

@Composable
internal fun MovementsTab(
    date: String,
    total: Int,
    chipSelected: MovementsFilterChip,
    labelSelected: String?,
    movements: List<MovementItemEntity>,
    eventHandler: (HomeEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    text = PriceHandler.formatPrice(total.absoluteValue, isLess = total < 0),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            FilterChipCategories(
                modifier = Modifier.fillMaxWidth(),
                chipList = MovementsFilterChip.entries,
                chipSelected = chipSelected,
                labelSelected = labelSelected,
            ){ eventHandler(HomeEvent.OnMovementFilterSelected(it)) }
        }

        items(movements) { movement ->
            MovementItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(6.dp),
                movement = movement
            )
        }
    }
}

@Composable
private fun FilterChipCategories(
    modifier: Modifier,
    chipList: List<MovementsFilterChip>,
    chipSelected: MovementsFilterChip,
    labelSelected: String?,
    onChipSelected: (MovementsFilterChip) -> Unit,
) = LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
    items(chipList) { item ->
        FilterChip(
            modifier = modifier.padding(
                start = if (item == chipList.first()) 12.dp else 0.dp,
                end = if (item == chipList.last()) 12.dp else 0.dp
            ),
            shape = CircleShape,
            border = if (item == chipSelected) BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            ) else null,
            colors = FilterChipDefaults.filterChipColors().copy(
                containerColor = Color.Gray.copy(alpha = 0.4f),
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                trailingIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary
            ),
            trailingIcon = {
                if (item == MovementsFilterChip.LABEL) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDropDown,
                        contentDescription = null
                    )
                }
            },
            selected = item == chipSelected,
            onClick = { onChipSelected(item) },
            label = {
                Text(
                    text = if (item == MovementsFilterChip.LABEL) labelSelected ?: item.text else item.text,
                    fontWeight = FontWeight.let { if (item == chipSelected) it.Black else it.Light }
                )
            }
        )
    }
}