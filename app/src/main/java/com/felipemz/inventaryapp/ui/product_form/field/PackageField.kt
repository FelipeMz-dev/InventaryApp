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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.ui.commons.CommonFormField
import com.felipemz.inventaryapp.ui.commons.actions.BillActions
import com.felipemz.inventaryapp.ui.commons.ProductsListBottomSheet
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.ui.product_form.components.ProductSelectedItem

@Composable
fun PackageField(
    modifier: Modifier,
    productList: List<ProductModel>,
    selectedProducts: List<BillItemChart>?,
    isEnabled: Boolean,
    onOpen: suspend () -> Unit,
    onClick: (BillItemChart) -> Unit,
    onSelect: (BillActions) -> Unit,
    toggle: (Boolean) -> Unit,
) {

    var showProductsListBottomSheet by remember { mutableStateOf(false) }

    showProductsListBottomSheet.ifTrue {
        ProductsListBottomSheet(
            productList = productList,
            selected = selectedProducts ?: emptyList(),
            onDismiss = { showProductsListBottomSheet = false },
            onAction = onSelect
        )
    }

    CommonFormField(
        modifier = modifier,
        title = stringResource(R.string.copy_package_dots),
        isMandatory = false,
        visible = selectedProducts.isNotNull(),
        concealable = true,
        onOpen = onOpen,
        thumbContent = {
            Switch(
                enabled = isEnabled || selectedProducts.isNotNull(),
                checked = selectedProducts.isNotNull(),
                onCheckedChange = { state ->
                    toggle(selectedProducts.isNull())
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
                text = stringResource(R.string.copy_content),
                color = MaterialTheme.colorScheme.outline
            )

            ProductSelectionField(
                packageProducts = selectedProducts,
                onAdd = { showProductsListBottomSheet = true },
                onClick = onClick,
                onAction = onSelect
            )
        }
    }
}

@Composable
private fun ProductSelectionField(
    packageProducts: List<BillItemChart>?,
    onAdd: () -> Unit,
    onClick: (BillItemChart) -> Unit,
    onAction: (BillActions) -> Unit
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
                    amount = product,
                    onClick = { onClick(product) },
                    onAction = { onAction(it) },
                )
            } else Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.copy_not_products_related)
            )
        }

        TextButtonUnderline(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.copy_add_products)
        ) { onAdd() }
    }
}