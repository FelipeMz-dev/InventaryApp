package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.CalculatorKey
import com.felipemz.inventaryapp.core.extensions.tryOrDefault
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CalculatorBottomSheet(
    currentQuantity: Int,
    onDismiss: () -> Unit,
    onSelect: (Int) -> Unit,
) {

    var quantity by remember { mutableIntStateOf(0) }
    val sum by remember(quantity) {
        derivedStateOf {
            if (currentQuantity > 0) {
                (currentQuantity + quantity).let { if (it > 0) it else null }
            } else null
        }
    }
    val state = rememberModalBottomSheetState()
    val cursorPosition = remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        state.expand()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = state
    ) {

        CustomTextFieldWithoutKeyboard(
            value = quantity.toString(),
            cursorPosition = cursorPosition,
            onDone = {}
        )

        Column {

            CalculatorKeyboard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) { key ->
                when (key) {
                    CalculatorKey.DELETE -> {
                        quantity = quantity.toString().dropLast(1).toIntOrNull() ?: 0
                        cursorPosition.value = (cursorPosition.value - 1).takeIf { cursorPosition.value > 1 } ?: 1
                    }
                    CalculatorKey.HISTORY -> {}
                    CalculatorKey.CLEAR -> {
                        quantity = 0
                        cursorPosition.value = 1
                    }
                    CalculatorKey.CLEAR_ALL -> {
                        quantity = 0
                        cursorPosition.value = 1
                    }
                    else -> {
                        quantity = tryOrDefault(quantity) {
                            if (cursorPosition.value != quantity.toString().length) {
                                StringBuilder(quantity.toString()).insert(cursorPosition.value, key.key).toString().toInt()
                            } else {
                                (quantity.toString() + key.key).toInt()
                            }.also {
                                cursorPosition.value += key.key.length.takeUnless { quantity == 0 } ?: 0
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomTextFieldWithoutKeyboard(
    value: String,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    cursorPosition: MutableState<Int>,
    onDone: () -> Unit,
) {


    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val widthLetter = with(LocalDensity.current) { textStyle.fontSize.toPx() / 1.65f }

    var isVisible by remember { mutableStateOf(true) }
    val alpha by remember { derivedStateOf { if (isVisible) 1f else 0f } }
    val cursorColor = MaterialTheme.colorScheme.primary.copy(alpha = alpha)

    LaunchedEffect(Unit) {
        while (true) {
            delay(800)
            isVisible = !isVisible
        }
    }

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .drawWithContent {
                    drawRect(
                        color = cursorColor,
                        topLeft = Offset(widthLetter * cursorPosition.value, 0f),
                        size = Size(2.dp.toPx(), size.height)
                    )
                    drawContent()
                }
                .pointerInput(Unit) {
                    detectTapGestures {
                        cursorPosition.value = (it.x / widthLetter).toInt()
                    }
                },
            text = value,
            style = textStyle,
            fontFamily = FontFamily.Monospace,
        )

        IconButton(
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = { keyboardController?.hide() }
        ) {
            Icon(
                modifier = Modifier,
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_send_calculator),
                contentDescription = null
            )
        }
    }
}

@Composable
fun CalculatorKeyboard(
    modifier: Modifier,
    onKeyPress: (CalculatorKey) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(4)
    ) {
        items(CalculatorKey.entries) {
            ItemButtonCalculator(
                item = it,
                onKeyPress = onKeyPress
            )
        }
    }
}

@Composable
private fun ItemButtonCalculator(
    item: CalculatorKey,
    onKeyPress: (CalculatorKey) -> Unit,
) {

    val size = with(LocalDensity.current) {
        MaterialTheme.typography.headlineSmall.lineHeight.toDp()
    }

    Button(
        onClick = { onKeyPress(item) },
        modifier = Modifier.padding(4.dp),
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
            else -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    ) {
        when (item) {
            CalculatorKey.DELETE -> Icon(
                modifier = Modifier.size(size),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                contentDescription = null
            )
            CalculatorKey.HISTORY -> Icon(
                modifier = Modifier.size(size),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_history),
                contentDescription = null
            )
            else -> Text(
                text = item.key,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}