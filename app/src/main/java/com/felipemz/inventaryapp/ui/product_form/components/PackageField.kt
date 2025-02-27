package com.felipemz.inventaryapp.ui.product_form.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.ProductQuantityEntity
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem

@Composable
fun PackageField(
    modifier: Modifier,
    packageProduct: ProductQuantityEntity?,
    isNotQuantity: Boolean,
    isNotComposition: Boolean,
    onAdd: () -> Unit,
    onOpen: suspend () -> Unit,
    onClick: (ProductQuantityEntity) -> Unit,
    onSelect: (ProductQuantityEntity?) -> Unit
) {
    CommonFormField(
        modifier = modifier,
        title = stringResource(R.string.copy_package_dots),
        isMandatory = false,
        visible = packageProduct.isNotNull(),
        concealable = true,
        onOpen = onOpen,
        thumbContent = {
            Switch(
                enabled = isNotQuantity && isNotComposition || packageProduct.isNotNull(),
                checked = packageProduct.isNotNull(),
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
                text = "Producto:",
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
                packageProduct?.let { pack ->
                    pack.product?.let { product ->
                        ProductPackageItem(
                            product = pack,
                            onClick = { onClick(pack) },
                            onChangeSelection = { value ->
                                onSelect(ProductQuantityEntity(product, value))
                            },
                            onDelete = {
                                onSelect(ProductQuantityEntity(product, 0))
                            }
                        )
                    } ?: Text(
                        modifier = Modifier.padding(8.dp),
                        text = "No hay producto relacionado"
                    )
                }
            }

            TextButtonUnderline(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(
                    id = packageProduct?.product?.let {
                        R.string.copy_change_product
                    } ?: R.string.copy_select_product
                )
            ) { onAdd() }
        }
    }
}

@Composable
fun ProductPackageItem(
    product: ProductQuantityEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onChangeSelection: (Int) -> Unit
) {

    val state = rememberSwipeToDismissBoxState()

    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete()
            state.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    SwipeToDismissBox(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp),
        state = state,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Icon(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Default.Delete,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null
            )
        }
    ) {

        ProductItem(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            product = product.product ?: ProductEntity(),
            selection = product.quantity,
            onSelectionChange = { onChangeSelection(it) }
        )
    }
}