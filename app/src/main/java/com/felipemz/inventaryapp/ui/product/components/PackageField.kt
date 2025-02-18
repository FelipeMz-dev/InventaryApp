package com.felipemz.inventaryapp.ui.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.entitys.PackageProductModel
import com.felipemz.inventaryapp.core.entitys.ProductPackEntity
import com.felipemz.inventaryapp.core.enums.PackageType
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline

@Composable
internal fun PackageField(
    modifier: Modifier,
    packageType: PackageProductModel?,
    isNotQuantity: Boolean,
    onAdd: () -> Unit,
    onOpen: suspend () -> Unit,
    onClick: (ProductPackEntity) -> Unit,
    onDelete: (ProductPackEntity) -> Unit,
    onSelect: (PackageProductModel?) -> Unit
) {

    CommonTitledColumn(
        modifier = modifier,
        title = stringResource(R.string.copy_agrupation_dots),
        isMandatory = false,
        visible = packageType.isNotNull(),
        concealable = true,
        onOpen = onOpen,
        thumbContent = {
            Switch(
                enabled = isNotQuantity,
                checked = packageType.isNotNull(),
                onCheckedChange = { state ->
                    onSelect(PackageProductModel.Package().takeIf { state })
                }
            )
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = stringResource(R.string.copy_type_dots),
                color = MaterialTheme.colorScheme.outline
            )

            PackageType.entries.forEach { type ->

                val packType = remember {
                    when (type) {
                        PackageType.PACKAGE -> PackageProductModel.Package()
                        PackageType.PACk -> PackageProductModel.Pack()
                    }
                }

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onSelect(packType) }
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RadioButton(
                        selected = type == packageType?.getPackageType(),
                        onClick = { onSelect(packType) }
                    )

                    Text(text = type.text)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Productos:",
                    color = MaterialTheme.colorScheme.outline
                )

                Text(
                    text = when (packageType) {
                        is PackageProductModel.Pack -> "Cantidades:"
                        is PackageProductModel.Package -> "Cantidad:"
                        else -> String()
                    },
                    color = MaterialTheme.colorScheme.outline,
                )
            }

            ProductRelated(
                modifier = Modifier.fillMaxWidth(),
                productType = packageType ?: PackageProductModel.Pack(),
                onClick = onClick,
                onDelete = onDelete
            )

            TextButtonUnderline(
                text = when (packageType) {
                    is PackageProductModel.Pack -> "Agregar productos"
                    is PackageProductModel.Package -> packageType.product?.let {
                        "Cambiar producto"
                    } ?: "Seleccionar producto"
                    else -> String()
                }
            ) { onAdd() }
        }
    }
}

@Composable
private fun ProductRelated(
    modifier: Modifier,
    productType: PackageProductModel,
    onClick: (ProductPackEntity) -> Unit,
    onDelete: (ProductPackEntity) -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 4.dp)
    ) {
        when (productType) {
            is PackageProductModel.Pack -> productType.products.let {
                it.forEach { product ->
                    ProductPackageItem(
                        product = product,
                        onClick = { onClick(product) }
                    ) { onDelete(product) }
                }
                it.ifEmpty {
                    Text(text = "No hay productos relacionados")
                }
            }
            is PackageProductModel.Package -> productType.product.let {
                it?.let { product ->
                    ProductPackageItem(
                        product = product,
                        onClick = { onClick(product) }
                    ) { onDelete(product) }
                } ?: Text(text = "No hay producto relacionado")
            }
        }
    }
}

@Composable
private fun ProductPackageItem(
    product: ProductPackEntity,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {

    val state = rememberSwipeToDismissBoxState()

    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDismiss()
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
                    .wrapContentWidth(Alignment.End),
                imageVector = Icons.Default.Delete,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = product.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 4.dp),
                text = "${product.quantity}/${product.quantityType?.initial}"
            )
        }
    }
}