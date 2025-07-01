package com.felipemz.inventaryapp.ui.product_form.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.felipemz.inventaryapp.ui.commons.InvoiceActions
import com.felipemz.inventaryapp.ui.commons.InvoiceActions.*
import com.felipemz.inventaryapp.ui.commons.ProductInvoiceItem
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductQuantityActionType

@Composable
fun ProductSelectedItem(
    product: ProductInvoiceItem,
    onClick: () -> Unit,
    onAction: (InvoiceActions) -> Unit
) {

    val state = rememberSwipeToDismissBoxState()

    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onAction(OnRemoveItem(product))
            state.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    SwipeToDismissBox(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                .background(MaterialTheme.colorScheme.surfaceContainer),
            isSmall = true,
            product = product.product,
            selection = product.quantity,
            onQuantity = {
                when (it) {
                    ProductQuantityActionType.ADD -> onAction(OnAddItem(product))
                    ProductQuantityActionType.SUBTRACT -> onAction(OnSubtractItem(product))
                    ProductQuantityActionType.UPDATE -> {}
                }
            },
        )
    }
}