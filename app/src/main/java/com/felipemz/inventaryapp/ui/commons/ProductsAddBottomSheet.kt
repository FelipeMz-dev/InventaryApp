package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.ProductSelectedEntity
import com.felipemz.inventaryapp.core.extensions.ifNotEmpty
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsAddBottomSheet(
    productList: List<ProductEntity>,
    listSelected: List<ProductSelectedEntity>,
    onDismiss: () -> Unit,
    onSelect: (ProductEntity) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        LazyColumn {
            items(productList) { product ->
                ProductItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(product) },
                    product = product,
                )
            }
        }
    }
}