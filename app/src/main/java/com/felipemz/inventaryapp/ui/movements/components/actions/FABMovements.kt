package com.felipemz.inventaryapp.ui.movements.components.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R

@Composable
fun FABMovements(
    onAction: (MovementsActions) -> Unit,
    onOpenProducts: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.Companion
                .width(IntrinsicSize.Min)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = CircleShape
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MovementsActions.entries.forEach {
                IconButton(onClick = { onAction(it) }) {
                    Icon(
                        imageVector = ImageVector.Companion.vectorResource(id = it.resId),
                        contentDescription = null
                    )
                }
            }
        }

        FloatingActionButton(onClick = onOpenProducts) {
            Icon(
                imageVector = ImageVector.Companion.vectorResource(R.drawable.ic_product_list),
                contentDescription = null
            )
        }
    }
}