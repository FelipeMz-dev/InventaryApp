package com.felipemz.inventaryapp.ui.product_form.field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.ui.commons.CommonFormField
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline

@Composable
internal fun QuantityField(
    modifier: Modifier,
    quantityType: QuantityType?,
    isEnabled: Boolean,
    quantity: Int = 0,
    onAdd: () -> Unit,
    onOpen: suspend () -> Unit,
    onSelect: (QuantityType?) -> Unit = {},
) {

    var showListPopup by remember { mutableStateOf(false) }
    val toggle by remember(quantityType, isEnabled) {
        derivedStateOf { quantityType.isNull().not() || !isEnabled }
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

        QuantityTypeField(
            quantityType = quantityType,
            enabled = isEnabled
        ) { showListPopup = true }

        QuantityValueField(
            quantity = quantity,
            quantityType = quantityType,
            enabled = isEnabled
        ) { onAdd() }

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