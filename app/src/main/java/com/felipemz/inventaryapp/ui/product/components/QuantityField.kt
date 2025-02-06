package com.felipemz.inventaryapp.ui.product.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.isNull

@Composable
internal fun QuantityField(
    modifier: Modifier,
    quantityType: QuantityType?,
    quantity: Int = 0,
    onAdd: () -> Unit,
    onSelect: (QuantityType?) -> Unit = {},
) {

    val toggle by remember(quantityType) { derivedStateOf { quantityType.isNull().not() } }
    var showListPopup by remember { mutableStateOf(false) }

    CommonTitledColumn(
        modifier = modifier,
        title = stringResource(R.string.copy_quantities_dots),
        concealable = true,
        isMandatory = false,
        visible = toggle,
        thumbContent = {
            Switch(
                checked = toggle,
                onCheckedChange = {
                    if (it) onSelect(QuantityType.UNIT) else onSelect(null)
                }
            )
        }
    ) {

        QuantityTypeField(quantityType) { showListPopup = true }

        QuantityValueField(
            quantity = quantity,
            quantityType = quantityType
        ) { onAdd() }

        DropDownQuantityType(
            showListPopup = showListPopup,
            onDismiss = { showListPopup = false },
        ) {
            onSelect(it)
            showListPopup = false
        }
    }
}

@Composable
private fun DropDownQuantityType(
    showListPopup: Boolean,
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
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .clickable { onSelect(type) }
                    .padding(horizontal = 8.dp),
                text = type.text,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
private fun QuantityValueField(
    quantity: Int,
    quantityType: QuantityType?,
    action: () -> Unit,
) {
    Row {

        Text(
            text = stringResource(R.string.copy_quantity_dots),
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { action() }
                .padding(start = 8.dp),
            text = "$quantity/${quantityType?.initial.orEmpty()}",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
private fun QuantityTypeField(
    quantityType: QuantityType?,
    action: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(R.string.copy_quantity_type_dots),
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { action() }
                .padding(horizontal = 8.dp),
            text = quantityType?.text.orEmpty(),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )
    }
}