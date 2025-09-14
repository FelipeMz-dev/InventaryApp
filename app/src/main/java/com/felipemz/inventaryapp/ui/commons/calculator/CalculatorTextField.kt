package com.felipemz.inventaryapp.ui.commons.calculator

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.EMPTY_STRING
import com.felipemz.inventaryapp.core.extensions.thenIf
import com.felipemz.inventaryapp.core.utils.CurrencyUtil
import kotlinx.coroutines.delay

@Composable
fun CalculatorTextField(
    value: Int,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    leadingText: String? = null,
    result: CalculatorResult,
    cursorPosition: Int,
    enable: Boolean = true,
    onMoveCursor: (Int) -> Unit,
    onDone: () -> Unit,
) {

    val textValue = CurrencyUtil.formatWithoutCurrency(value)

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                .padding(vertical = 8.dp, horizontal = 12.dp),
        ) {

            Text(
                text = "${CurrencyUtil.formatWithoutCurrency(result.first)} ${result.second.key}",
                style = textStyle.copy(
                    fontSize = textStyle.fontSize * 0.5f,
                    lineHeight = textStyle.fontSize * 0.5f
                ),
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                leadingText?.let {
                    Text(
                        text = it,
                        style = textStyle.copy(fontSize = textStyle.fontSize * 0.8f),
                        color = MaterialTheme.colorScheme.outline,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextWithCursor(
                    enable = enable,
                    cursorPosition = cursorPosition,
                    value = textValue,
                    textStyle = textStyle
                ) { onMoveCursor(it) }
            }
        }

        IconButton(
            modifier = Modifier.size(54.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = { onDone() }
        ) {
            Icon(
                modifier = Modifier.size(34.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_send_calculator),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun TextWithCursor(
    enable: Boolean,
    cursorPosition: Int,
    value: String,
    textStyle: TextStyle,
    onMoveCursor: (Int) -> Unit,
) {

    var isVisible by remember { mutableStateOf(true) }
    val alpha by remember { derivedStateOf { if (isVisible) 1f else 0f } }
    val cursorColor = MaterialTheme.colorScheme.primary.copy(alpha = alpha)
    val widthLetter = with(LocalDensity.current) { textStyle.fontSize.toPx() / 1.66f }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            isVisible = !isVisible
        }
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .thenIf(
                condition = enable,
                modifier = Modifier
                    .drawWithContent {
                        val textUntilCursor = value.substring(0, cursorPosition.coerceAtMost(value.length))
                        val positionWithDots = cursorPosition + textUntilCursor.count { it == '.'}
                        val xCursor = (widthLetter * positionWithDots)
                        drawRect(
                            color = cursorColor,
                            topLeft = Offset(xCursor, 0f),
                            size = Size(2.dp.toPx(), size.height)
                        )
                        drawContent()
                    }
                    .pointerInput(value.length) {
                        detectTapGestures { gesture ->
                            val position = (gesture.x / widthLetter).toInt()
                            val textUntilCursor = value.substring(0, position.coerceAtMost(value.length))
                            println("Text until cursor: $textUntilCursor")
                            val positionWithOutDots = position - textUntilCursor.count { it == '.'}
                            println("dots: ${textUntilCursor.count { it == '.' }}")
                            println("Position with out dots: $positionWithOutDots")
                            onMoveCursor(positionWithOutDots.coerceAtMost(value.length))
                        }
                    }
            ),
        text = if (enable) value else EMPTY_STRING,
        style = textStyle,
        fontFamily = FontFamily.Companion.Monospace,
    )
}

typealias CalculatorResult = Pair<Int, CalculatorKey>