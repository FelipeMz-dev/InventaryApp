package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.ui.commons.InvoiceActions.*
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductQuantityActionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsListBottomSheet(
    productList: List<ProductModel>,
    selected: List<ProductInvoiceItem>,
    onDismiss: () -> Unit,
    onAction: (InvoiceActions) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn {
            items(productList) { product ->

                val selection = remember(selected) {
                    selected.find { it.product.id == product.id }
                }

                val item = @Composable {
                    ProductItem(
                        modifier = Modifier.fillMaxWidth(),
                        product = product,
                        selection = selection?.quantity,
                        onQuantity = { action ->
                            selection?.let {
                                when (action) {
                                    ProductQuantityActionType.ADD -> onAction(OnAddItem(it))
                                    ProductQuantityActionType.SUBTRACT -> onAction(OnSubtractItem(it))
                                    ProductQuantityActionType.UPDATE -> {}
                                }
                            }
                        }
                    ) { onAction(OnInsertItem(ProductInvoiceItem(product))) }
                }

                selection?.let {
                    SwiperItemProduct(
                        onDelete = { onAction(OnRemoveItem(it)) }
                    ) { item() }
                } ?: item()
            }
        }
    }
}

@Composable
private fun SwiperItemProduct(
    onDelete: () -> Unit,
    content: @Composable () -> Unit,
) {

    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) onDelete()
            true
        }
    )

    SwipeToDismissBox(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Icon(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .wrapContentWidth(Alignment.End)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Default.Delete,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null
            )
        }
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceContainerHigh)) {
            content()
        }
    }
}