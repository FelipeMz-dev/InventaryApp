package com.felipemz.inventaryapp.ui.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope

fun Modifier.scanAnimation(): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "scan")
    val animX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing, delayMillis = 2000),
            repeatMode = RepeatMode.Restart,
        ),
        label = "scanX"
    )
    this.then(
        Modifier.Companion
            .drawWithContent {
                drawContent()
                val width = size.width
                val scanWidth = width * 0.18f
                val x = animX * (width - scanWidth)
                drawBarcodeGuidelines()
                drawRect(
                    brush = Brush.Companion.horizontalGradient(
                        colors = listOf(
                            Color.Companion.Transparent,
                            Color.Companion.Red.copy(alpha = 0.1f),
                            Color.Companion.Red.copy(alpha = 0.3f),
                            Color.Companion.Red.copy(alpha = 0.7f),
                            Color.Companion.Transparent
                        ),
                        startX = x,
                        endX = x + scanWidth,
                    ),
                    alpha = 0.8f,
                    topLeft = Offset(center.x - 200, center.y - 100),
                    size = Size(400f, 200f)
                )
            }
    )
}

private fun ContentDrawScope.drawBarcodeGuidelines() {
    val color = Color.Companion.White
    val centerX = size.width / 2
    val centerY = size.height / 2
    val alpha = 0.6f
    val strokeWidth = 6f
    drawLine(
        color = color,
        start = Offset(centerX - 200, centerY - 100),
        end = Offset(centerX - 150, centerY - 100),
        strokeWidth = strokeWidth,
        alpha = alpha
    )
    drawLine(
        color = color,
        start = Offset(centerX + 150, centerY - 100),
        end = Offset(centerX + 200, centerY - 100),
        strokeWidth = strokeWidth,
        alpha = alpha
    )
    drawLine(
        color = color,
        start = Offset(centerX - 200, centerY + 100),
        end = Offset(centerX - 150, centerY + 100),
        strokeWidth = strokeWidth,
        alpha = alpha
    )
    drawLine(
        color = color,
        start = Offset(centerX + 150, centerY + 100),
        end = Offset(centerX + 200, centerY + 100),
        strokeWidth = strokeWidth,
        alpha = alpha
    )
    drawLine(
        color = color,
        start = Offset(centerX - 200, centerY - 100),
        end = Offset(centerX - 200, centerY - 50),
        strokeWidth = strokeWidth,
        alpha = alpha
    )
    drawLine(
        color = color,
        start = Offset(centerX + 200, centerY - 100),
        end = Offset(centerX + 200, centerY - 50),
        strokeWidth = strokeWidth,
        alpha = alpha
    )
    drawLine(
        color = color,
        start = Offset(centerX - 200, centerY + 100),
        end = Offset(centerX - 200, centerY + 50),
        strokeWidth = strokeWidth,
        alpha = alpha
    )
    drawLine(
        color = color,
        start = Offset(centerX + 200, centerY + 100),
        end = Offset(centerX + 200, centerY + 50),
        strokeWidth = strokeWidth,
        alpha = alpha
    )
}