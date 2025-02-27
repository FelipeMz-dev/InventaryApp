package com.felipemz.inventaryapp.ui.product_form.components

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
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.entitys.ProductQuantityEntity
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline

@Composable
fun CompositionField(
    modifier: Modifier,
    compositionProducts: List<ProductQuantityEntity>?,
    isNotQuantity: Boolean,
    isNotPackage: Boolean,
    onAdd: () -> Unit,
    onOpen: suspend () -> Unit,
    onClick: (ProductQuantityEntity) -> Unit,
    onSelect: (ProductQuantityEntity?) -> Unit
) {
    CommonFormField(
        modifier = modifier,
        title = "Conjunto:",
        isMandatory = false,
        visible = compositionProducts.isNotNull(),
        concealable = true,
        onOpen = onOpen,
        thumbContent = {
            Switch(
                enabled = isNotQuantity && isNotPackage || compositionProducts.isNotNull(),
                checked = compositionProducts.isNotNull(),
                onCheckedChange = { state ->
                    onSelect(ProductQuantityEntity().takeIf { state })
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
                text = "Productos:",
                color = MaterialTheme.colorScheme.outline
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                compositionProducts?.let { composition ->
                    if (composition.isNotEmpty()) composition.forEach { product ->
                        ProductPackageItem(
                            product = product,
                            onClick = { onClick(product) },
                            onChangeSelection = { value ->
                                onSelect(product.copy(quantity = value))
                            },
                            onDelete = {
                                onSelect(product.copy(quantity = 0))
                            }
                        )
                    } else Text(
                        modifier = Modifier.padding(8.dp),
                        text = "No hay productos relacionados"
                    )
                }
            }

            TextButtonUnderline(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Agregar productos"
            ) { onAdd() }
        }
    }
}