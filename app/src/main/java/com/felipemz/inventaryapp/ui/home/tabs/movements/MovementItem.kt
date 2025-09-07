package com.felipemz.inventaryapp.ui.home.tabs.movements

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felipemz.inventaryapp.domain.model.MovementModel
import com.felipemz.inventaryapp.core.enums.MovementItemType
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.utils.CurrencyUtil

@Composable
fun MovementItem(
    modifier: Modifier,
    movement: MovementModel,
    movementColor: Color
) {

    Row(
        modifier = modifier.padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .background(
                    color = movementColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(8.dp)
                .size(32.dp)
                .graphicsLayer(scaleY = movement.type.scaleY),
            imageVector = ImageVector.vectorResource(movement.type.icon),
            tint = movementColor,
            contentDescription = null
        )

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(Modifier.fillMaxWidth()) {

                Text(
                    modifier = Modifier.weight(1f),
                    text = movement.number?.let {
                        "${movement.type.text} #${it}"
                    } ?: movement.type.text,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = movement.time,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                movement.labels.isEmpty().ifTrue {
                    Text(text = "Sin etiquetas", color = Color.Gray)
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .clip(CircleShape)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    movement.labels.forEach { label ->
                        Text(
                            modifier = Modifier
                                .background(
                                    color = Color.Gray.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .padding(horizontal = 6.dp, vertical = 1.dp),
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                Text(
                    modifier = Modifier
                        .background(
                            color = movementColor.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .padding(horizontal = 6.dp),
                    text = CurrencyUtil.formatPrice(movement.total, isLess = movement.type == MovementItemType.MOVEMENT_EXPENSE),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}