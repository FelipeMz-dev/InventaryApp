package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalDotDivider(
    modifier: Modifier,
    width: Dp = 1.dp,
    space: Dp = 4.dp,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
) {
    Canvas(modifier = modifier.height(1.dp)) {

        val dotRadius = width.toPx() / 2
        val dotSpacing = space.toPx()
        val dots = size.width / (dotRadius * 2 + dotSpacing)

        for (i in 0 until dots.toInt()) {
            drawCircle(
                color = color,
                radius = dotRadius,
                center = Offset(
                    x = (i * (dotRadius * 2 + dotSpacing)) + dotRadius,
                    y = size.height / 2
                )
            )
        }
    }
}