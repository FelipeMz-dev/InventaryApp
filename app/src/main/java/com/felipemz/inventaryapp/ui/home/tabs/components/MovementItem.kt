package com.felipemz.inventaryapp.ui.home.tabs.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.ifTrue

@Composable
fun MovementItem(
    modifier: Modifier,
    movement: MovementItemEntity,
) {
    Row(
        modifier = modifier.padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(8.dp)
                .size(32.dp)
                .graphicsLayer(scaleY = movement.type.scaleY),
            painter = painterResource(movement.type.icon),
            tint = colorResource(id = movement.type.color),
            contentDescription = null
        )

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(Modifier.fillMaxWidth()) {

                Text(
                    modifier = Modifier.weight(1f),
                    text = movement.number?.let {
                        "${movement.type.text} #${it}"
                    } ?: movement.type.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                    )
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
                            color = colorResource(movement.type.color).copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                        .padding(horizontal = 6.dp),
                    text = formatPrice(movement.amount, isLess = movement.type == MovementType.MOVEMENT_EXPENSE),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class MovementItemEntity(
    val type: MovementType = MovementType.MOVEMENT_PENDING,
    val number: Int? = null,
    val date: String = String(),
    val time: String = String(),
    val amount: Int = 0,
    val labels: List<String> = emptyList(),
)

enum class MovementType(
    val text: String,
    val icon: Int,
    val color: Int,
    val scaleY: Float,
) {
    MOVEMENT_SALE("Venta", R.drawable.ic_movement_up, R.color.blue, 1f),
    MOVEMENT_EXPENSE("Gasto", R.drawable.ic_movement_up, R.color.orange, -1f),
    MOVEMENT_PENDING("Pendiente", R.drawable.ic_movement_pending, R.color.deep_red, 1f),
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    Column {
        MovementItem(
            modifier = Modifier.fillMaxWidth(),
            movement = MovementItemEntity(
                type = MovementType.MOVEMENT_SALE,
                number = 1,
                date = "12/12/2021",
                time = "12:00 am",
                amount = 10000,
                labels = listOf("label1", "label2")
            )
        )
        MovementItem(
            modifier = Modifier.fillMaxWidth(),
            movement = MovementItemEntity(
                type = MovementType.MOVEMENT_EXPENSE,
                number = 1,
                date = "12/12/2021",
                time = "1:00 pm",
                amount = 10000,
                labels = listOf("rappy", "domicilios", "mesa 1", "mesa 2", "caja 1", "caja 2")
            )
        )
        MovementItem(
            modifier = Modifier.fillMaxWidth(),
            movement = MovementItemEntity(
                type = MovementType.MOVEMENT_PENDING,
                date = "12/12/2021",
                time = "2:00 pm",
                amount = 10000
            )
        )
    }
}