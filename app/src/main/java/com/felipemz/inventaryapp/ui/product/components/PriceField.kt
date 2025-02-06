package com.felipemz.inventaryapp.ui.product.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.coerceIn
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.tryOrDefault
import com.felipemz.inventaryapp.core.utils.PriceUtil

@Composable
internal fun PriceField(
    modifier: Modifier,
    value: Int,
    onChange: (Int) -> Unit
) {

    var text by remember(value) { mutableStateOf(PriceUtil.formatPrice(value)) }
    var selection by remember { mutableStateOf(TextRange.Zero) }

    CommonTitledColumn(
        modifier = modifier,
        title = stringResource(R.string.copy_price_dots)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            value = TextFieldValue(
                text = text,
                selection = selection
            ),
            onValueChange = { newText ->
                val newValue = PriceUtil.getValue(newText.text, value)
                val newValueText = PriceUtil.formatPrice(newValue)
                val setSelection: (Int) -> Unit = { sum ->
                    selection = tryOrDefault(TextRange.Zero) {
                        TextRange(newText.selection.start + sum).coerceIn(2, newValueText.length)
                    }
                }
                if (newValue != value) {
                    setSelection(
                        when {
                            newValueText.length > newText.text.length -> 1
                            newValueText.length < newText.text.length -> -1
                            else -> 0
                        }
                    )
                    text = newValueText
                    onChange(newValue)
                } else {
                    setSelection(0)
                }
            },
            trailingIcon = {
                CommonTrailingIcon(value == 0) {
                    val newValue = PriceUtil.getValue(it)
                    onChange(newValue)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}