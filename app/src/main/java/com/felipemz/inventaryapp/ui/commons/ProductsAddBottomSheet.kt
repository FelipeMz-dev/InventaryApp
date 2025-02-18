package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.felipemz.inventaryapp.core.entitys.PackageProductModel
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.ProductSelectionEntity
import com.felipemz.inventaryapp.core.entitys.toProductSelectedEntity
import com.felipemz.inventaryapp.core.extensions.ifFalse
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsAddBottomSheet(
    productList: List<ProductEntity>,
    selected: PackageProductModel?,
    onDismiss: () -> Unit,
    onSelect: (ProductSelectionEntity) -> Unit,
) {

    val currentSelected = remember(selected) {
        when (selected) {
            is PackageProductModel.Pack -> selected.products.map { it }
            is PackageProductModel.Package -> listOfNotNull(selected.product)
            else -> emptyList()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        LazyColumn {
            items(productList) { product ->
                ProductItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (selected is PackageProductModel.Package) onDismiss()
                            onSelect(product.toProductSelectedEntity(1))
                        },
                    product = product,
                    selection = currentSelected.find { it.id == product.id }?.toProductSelectedEntity(),
                    onSelectionChange = { onSelect(it) },
                )
            }
        }
    }
}