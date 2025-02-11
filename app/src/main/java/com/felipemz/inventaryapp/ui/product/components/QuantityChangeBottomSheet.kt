package com.felipemz.inventaryapp.ui.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.ifNotNull
import com.felipemz.inventaryapp.core.extensions.tryOrDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuantityChangeBottomSheet(
    currentQuantity: Int,
    quantityType: QuantityType,
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

    ModalBottomSheet(onDismissRequest = onDismiss) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f),
                shape = RoundedCornerShape(12.dp),
                value = quantity.toString(),
                onValueChange = {
                    quantity = tryOrDefault(quantity) { it.toInt() }
                },
                trailingIcon = {
                    CommonTrailingIcon(quantity == 0) {
                        quantity = tryOrDefault(0) { it.toInt() }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Actions(
                quantity = quantity,
                sum = sum,
                quantityType = quantityType,
                onSelect = onSelect
            )
        }
    }
}

@Composable
private fun Actions(
    quantity: Int,
    sum: Int?,
    quantityType: QuantityType,
    onSelect: (Int) -> Unit,
) {

    val enabled by remember(quantity) { derivedStateOf { quantity > 0 } }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            modifier = Modifier
                .clip(CircleShape)
                .background(if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer)
                .clickable(enabled = enabled) { onSelect(quantity) }
                .padding(horizontal = 8.dp, vertical = 2.dp),
            text = "Asignar $quantity/${quantityType.initial}",
            style = MaterialTheme.typography.bodyMedium,
            color = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )

        sum.ifNotNull {
            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer)
                    .clickable(enabled = enabled) { onSelect(it) }
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                text = "Sumar $it/${quantityType.initial}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}