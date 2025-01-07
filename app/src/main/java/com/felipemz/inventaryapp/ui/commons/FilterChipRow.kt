package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun <T> FilterChipRow(
    modifier: Modifier,
    text: @Composable (T) -> String,
    colors: @Composable (T) -> SelectableChipColors = {
        FilterChipDefaults.filterChipColors().copy(
            containerColor = Color.Gray.copy(alpha = 0.4f),
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary
        )
    },
    trailingIcon: ((T) -> (@Composable () -> Unit)?)? = null,
    border: ((T) -> BorderStroke?)? = null,
    chipList: List<T>,
    chipSelected: T?,
    onSelectChip: (T) -> Unit,
) = LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
    items(chipList) { item ->
        FilterChip(
            modifier = modifier.padding(
                start = if (item == chipList.first()) 12.dp else 0.dp,
                end = if (item == chipList.last()) 12.dp else 0.dp
            ),
            shape = CircleShape,
            border = border?.invoke(item),
            colors = colors(item),
            trailingIcon = trailingIcon?.invoke(item),
            selected = item == chipSelected,
            onClick = { onSelectChip(item) },
            label = {
                Text(
                    text = text(item),
                    fontWeight = if (item == chipSelected) FontWeight.Black else FontWeight.Light
                )
            }
        )
    }
}