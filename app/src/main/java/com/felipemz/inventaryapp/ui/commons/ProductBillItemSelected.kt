package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.ui.commons.actions.BillActions
import com.felipemz.inventaryapp.ui.commons.actions.BillActions.*
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductQuantityActionType
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductBillItem

@Composable
fun ProductBillItemSelected(
    item: BillItemChart,
    showTotal: Boolean = true,
    onClick: () -> Unit,
    onOpenCalculator: () -> Unit,
    onAction: (BillActions) -> Unit
) {

    val state = rememberSwipeToDismissBoxState()

    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onAction(OnRemoveItem(item))
            state.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    SwipeToDismissBox(
        modifier = Modifier.fillMaxWidth(),
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

        ProductBillItem(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            showTotal = showTotal,
            product = item.product ?: ProductModel(price = item.value),
            quantity = item.quantity,
            onQuantity = {
                when (it) {
                    ProductQuantityActionType.ADD -> onAction(OnAddItem(item))
                    ProductQuantityActionType.SUBTRACT -> onAction(OnSubtractItem(item))
                    ProductQuantityActionType.UPDATE -> onOpenCalculator()
                }
            },
            onClick = onClick
        )
    }
}