package com.felipemz.inventaryapp.ui.commons.calculator

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.ifTrue

@Composable
fun CalculatorKeyboard(
    modifier: Modifier,
    enable: Boolean = true,
    hasHistory: Boolean = false,
    onKeyPress: (CalculatorKey) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(4)
    ) {
        items(CalculatorKey.entries) {
            ItemButtonCalculator(
                item = it,
                isEnable = if (it == CalculatorKey.HISTORY) hasHistory else true,
                onKeyPress = {
                    if (!it.isNumber()) onKeyPress(it)
                    else enable.ifTrue { onKeyPress(it) }
                }
            )
        }
    }
}

@Composable
private fun ItemButtonCalculator(
    item: CalculatorKey,
    isEnable: Boolean,
    onKeyPress: (CalculatorKey) -> Unit,
) {

    val size = with(LocalDensity.current) {
        MaterialTheme.typography.headlineSmall.lineHeight.toDp()
    }

    Button(
        onClick = { onKeyPress(item) },
        enabled = isEnable,
        modifier = Modifier.Companion.padding(4.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        colors = when (item.themeNumber) {
            1 -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            2 -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
            3 -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
            else -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    ) {
        when (item) {
            CalculatorKey.DELETE -> Icon(
                modifier = Modifier.Companion.size(size),
                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_delete),
                contentDescription = null
            )
            CalculatorKey.HISTORY -> Icon(
                modifier = Modifier.Companion.size(size),
                imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_history),
                contentDescription = null
            )
            else -> Text(
                text = item.key,
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = FontFamily.Companion.Monospace,
                fontWeight = FontWeight.Companion.Bold.takeIf { item.themeNumber > 0 }
            )
        }
    }
}