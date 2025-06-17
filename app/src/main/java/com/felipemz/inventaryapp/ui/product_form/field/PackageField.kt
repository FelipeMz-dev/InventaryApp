package com.felipemz.inventaryapp.ui.product_form.field

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.domain.model.ProductSelectionChart
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.ui.commons.CommonFormField
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import com.felipemz.inventaryapp.ui.product_form.components.ProductSelectedItem

@Composable
fun PackageField(
    modifier: Modifier,
    selectedProducts: List<ProductSelectionChart>?,
    isEnabled: Boolean,
    onAdd: () -> Unit,
    onOpen: suspend () -> Unit,
    onClick: (ProductSelectionChart) -> Unit,
    onSelect: (ProductSelectionChart?) -> Unit
) {
    CommonFormField(
        modifier = modifier,
        title = "Paquete:",
        isMandatory = false,
        visible = selectedProducts.isNotNull(),
        concealable = true,
        onOpen = onOpen,
        thumbContent = {
            Switch(
                enabled = isEnabled || selectedProducts.isNotNull(),
                checked = selectedProducts.isNotNull(),
                onCheckedChange = { state ->
                    onSelect(ProductSelectionChart().takeIf { state })
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = "Sub productos:",
                color = MaterialTheme.colorScheme.outline
            )

            ProductSelectionField(
                packageProducts = selectedProducts,
                onAdd = onAdd,
                onClick = onClick,
                onSelect = onSelect
            )
        }
    }
}

@Composable
private fun ProductSelectionField(
    packageProducts: List<ProductSelectionChart>?,
    onAdd: () -> Unit,
    onClick: (ProductSelectionChart) -> Unit,
    onSelect: (ProductSelectionChart?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHigh),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        packageProducts?.let { pack ->
            if (pack.isNotEmpty()) pack.forEach { product ->
                ProductSelectedItem(
                    product = product,
                    onClick = { onClick(product) },
                    onChangeSelection = { value ->
                        onSelect(product.copy(quantity = value))
                    },
                    onQuantity = {},
                    onDelete = {
                        onSelect(product.copy(quantity = 0))
                    }
                )
            } else Text(
                modifier = Modifier.padding(8.dp),
                text = "No hay productos relacionados"
            )
        }

        TextButtonUnderline(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            text = "Agregar productos"
        ) { onAdd() }
    }
}