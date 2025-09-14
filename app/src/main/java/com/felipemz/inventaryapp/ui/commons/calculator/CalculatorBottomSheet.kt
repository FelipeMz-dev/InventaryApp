package com.felipemz.inventaryapp.ui.commons.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.extensions.orEmpty
import com.felipemz.inventaryapp.core.utils.CurrencyUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorBottomSheet(
    controller: CalculatorController = CalculatorController(0),
    onDismiss: () -> Unit,
    onSelect: (Int) -> Unit
) {

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        if (controller.listOperation.isNotEmpty()) {
            HistoryContent(
                modifier = Modifier.weight(1f, false),
                listOperation = controller.listOperation,
                showHistory = controller.showHistory
            )
        }

        CalculatorTextField(
            value = controller.value,
            cursorPosition = controller.cursorPosition,
            leadingText = controller.temporalResult?.let { result ->
                controller.temporalOperator?.let { "$result ${it.key}" } ?: result.toString()
            },
            result = controller.result,
            enable = controller.isEnableCalculator,
            onMoveCursor = { controller.cursorPosition = it },
            onDone = {
                controller.onKeyPress(CalculatorKey.EQUAL)
                onSelect(controller.result.first)
                onDismiss()
            }
        )

        CalculatorKeyboard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            enable = controller.isEnableCalculator,
            hasHistory = controller.listOperation.isNotEmpty()
        ) { controller.onKeyPress(it) }
    }
}

@Composable
private fun HistoryContent(
    modifier: Modifier,
    showHistory: Boolean,
    listOperation: List<MathOperation>
) {

    val scrollState = rememberScrollState()

    if (showHistory) {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(8.dp)
                .verticalScroll(scrollState)
        ) {
            listOperation.forEachIndexed { index, item ->
                val isLast = index == listOperation.lastIndex
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = CurrencyUtil.formatWithoutCurrency(item.first),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        textDecoration = if (isLast) TextDecoration.Underline else null
                    )
                    Text(
                        modifier = Modifier.weight(0.6f),
                        text = item.operator?.key.orEmpty(),
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.second.let { if (it == 0) "?" else CurrencyUtil.formatWithoutCurrency(it) },
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}