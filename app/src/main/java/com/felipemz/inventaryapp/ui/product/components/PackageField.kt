package com.felipemz.inventaryapp.ui.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.entitys.PackageProductType
import com.felipemz.inventaryapp.core.entitys.ProductPackEntity
import com.felipemz.inventaryapp.core.enums.PackageType
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline

@Composable
internal fun PackageField(
    modifier: Modifier,
    packageType: PackageProductType?,
    isNotQuantity: Boolean,
    onAdd: () -> Unit,
    onOpen: suspend () -> Unit,
    onClick: (ProductPackEntity) -> Unit,
    onSelect: (PackageProductType?) -> Unit
) {

    CommonTitledColumn(
        modifier = modifier,
        title = "Agrupación:",
        isMandatory = false,
        visible = packageType.isNotNull(),
        concealable = true,
        onOpen = onOpen,
        thumbContent = {
            Switch(
                enabled = isNotQuantity,
                checked = packageType.isNotNull(),
                onCheckedChange = { state ->
                    onSelect(PackageProductType.Package().takeIf { state })
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
                text = "Tipo:",
                color = MaterialTheme.colorScheme.outline
            )

            PackageType.entries.forEach { type ->

                val packType = remember {
                    when (type) {
                        PackageType.PACKAGE -> PackageProductType.Package()
                        PackageType.PACk -> PackageProductType.Pack()
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
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
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
                        is PackageProductType.Pack -> "Cantidades:"
                        is PackageProductType.Package -> "Cantidad:"
                        else -> String()
                    },
                    color = MaterialTheme.colorScheme.outline,
                )
            }

            ProductRelated(
                modifier = Modifier.fillMaxWidth(),
                productType = packageType ?: PackageProductType.Pack(),
                onClick = onClick
            )

            TextButtonUnderline(text = "Agregar") { onAdd() }
        }
    }
}

@Composable
private fun ProductRelated(
    modifier: Modifier,
    productType: PackageProductType,
    onClick: (ProductPackEntity) -> Unit,
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
            is PackageProductType.Pack -> productType.products.let {
                it.forEach { product ->
                    Text(
                        text = "${product.name} x${product.quantity}/${product.quantityType?.text}",
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onClick(product) }
                    )
                }
                it.ifEmpty {
                    Text(text = "No hay productos relacionados")
                }
            }
            is PackageProductType.Package -> productType.product.let {
                it?.let { product ->
                    Text(
                        text = "${product.name} x${product.quantity}/${product.quantityType?.text}",
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onClick(product) }
                    )
                } ?: Text(text = "No hay producto relacionado")
            }
        }
    }
}