package com.felipemz.inventaryapp.ui.movements.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.felipemz.inventaryapp.core.enums.MovementStateType
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.ui.product_form.field.getIdString

@Composable
internal fun IdMovementField(
    modifier: Modifier,
    idMovement: Int?,
    movementType: MovementStateType,
    action: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = getIdString(idMovement),
            fontWeight = FontWeight.Companion.Bold,
        )

        IconButton(onClick = action) {
            movementType.isDone().ifTrue {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
            }
        }
    }
}