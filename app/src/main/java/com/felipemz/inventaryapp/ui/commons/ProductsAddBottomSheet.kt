package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.ProductQuantityEntity
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsAddBottomSheet(
    productList: List<ProductEntity>,
    selected: List<ProductQuantityEntity>,
    onDismiss: () -> Unit,
    onSelect: (ProductQuantityEntity?) -> Unit,
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        LazyColumn {
            items(productList) { product ->

                val selectionValue = remember(selected) {
                    selected.find { it.product?.id == product.id }?.quantity
                }

                val item = @Composable {
                    ProductItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(
                                    ProductQuantityEntity(
                                        product = product,
                                        quantity = selectionValue?.let { it + 1 } ?: 1
                                    )
                                )
                            },
                        product = product,
                        selection = selectionValue,
                        onSelectionChange = { value ->
                            onSelect(
                                ProductQuantityEntity(
                                    product = product,
                                    quantity = value
                                )
                            )
                        },
                    )
                }

                selectionValue?.let {
                    SwiperItemProduct(
                        onDelete = { onSelect(ProductQuantityEntity(product, 0)) }
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