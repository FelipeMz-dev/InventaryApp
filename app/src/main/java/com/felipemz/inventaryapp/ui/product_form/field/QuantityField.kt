package com.felipemz.inventaryapp.ui.product_form.field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.extensions.tryOrDefault
import com.felipemz.inventaryapp.ui.commons.CalculatorBottomSheet
import com.felipemz.inventaryapp.ui.commons.CommonFormField
import com.felipemz.inventaryapp.ui.commons.CommonTrailingIcon
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline

@Composable
internal fun QuantityField(
    modifier: Modifier,
    quantityType: QuantityType?,
    isEnabled: Boolean,
    quantity: Int = 0,
    onOpen: suspend () -> Unit,
    onChange: (Int) -> Unit,
    onSelect: (QuantityType?) -> Unit = {},
) {

    var showCalculator by remember { mutableStateOf(false) }
    var showListPopup by remember { mutableStateOf(false) }
    val toggle by remember(quantityType, isEnabled) {
        derivedStateOf { quantityType.isNotNull() || !isEnabled }
    }

    showCalculator.ifTrue {
        CalculatorBottomSheet(
            currentQuantity = quantity,
            onDismiss = { showCalculator = false },
            onSelect = { onChange(it) }
        )
    }

    CommonFormField(
        modifier = modifier,
        title = stringResource(R.string.copy_quantities_dots),
        concealable = true,
        isMandatory = false,
        visible = toggle,
        onOpen = onOpen,
        thumbContent = {
            Switch(
                enabled = isEnabled,
                checked = toggle,
                onCheckedChange = {
                    if (it) onSelect(QuantityType.UNIT) else onSelect(null)
                }
            )
        }
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            enabled = isEnabled,
            shape = RoundedCornerShape(12.dp),
            value = quantity.toString(),
            onValueChange = {
                onChange(tryOrDefault(quantity) { it.toInt() })
            },
            leadingIcon = {
                Button(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    enabled = isEnabled,
                    shape = OutlinedTextFieldDefaults.shape,
                    onClick = {showListPopup = true}
                ) {
                    Text(
                        text = quantityType?.text.orEmpty(),
                        fontWeight = FontWeight.Bold,
                    )

                    Icon(
                        modifier = Modifier.padding(start = 4.dp),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    enabled = isEnabled,
                    onClick = { showCalculator = true }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_calculator),
                        contentDescription = null
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        DropDownQuantityType(
            showListPopup = showListPopup,
            selected = quantityType,
            onDismiss = { showListPopup = false }
        ) {
            onSelect(it)
            showListPopup = false
        }
    }
}

@Composable
private fun DropDownQuantityType(
    showListPopup: Boolean,
    selected: QuantityType?,
    onDismiss: () -> Unit,
    onSelect: (QuantityType?) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.padding(8.dp),
        expanded = showListPopup,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(8.dp),
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
    ) {
        QuantityType.entries.forEach { type ->
            Row(
                modifier = Modifier.clickable { onSelect(type) },
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = type == selected,
                    onClick = { onSelect(type) }
                )

                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = "${type.text} (${type.initial})",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuantityValueField(
    quantity: Int,
    quantityType: QuantityType?,
    enabled: Boolean,
    action: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Text(
            text = stringResource(R.string.copy_quantity_dots),
            color = MaterialTheme.colorScheme.outline
        )

        TextButtonUnderline(
            text = "$quantity/${quantityType?.initial.orEmpty()}",
            isEnabled = enabled,
            onClick = action
        )
    }
}

@Composable
private fun QuantityTypeField(
    quantityType: QuantityType?,
    enabled: Boolean,
    action: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Text(
            text = stringResource(R.string.copy_quantity_type_dots),
            color = MaterialTheme.colorScheme.outline
        )

        TextButtonUnderline(
            text = quantityType?.text.orEmpty(),
            isEnabled = enabled,
            onClick = action
        )
    }
}