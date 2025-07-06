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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.ui.commons.actions.BillActions
import com.felipemz.inventaryapp.ui.commons.actions.BillActions.*
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorBottomSheet
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorController
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductQuantityActionType
import com.felipemz.inventaryapp.core.charts.BillItemChart

@Composable
fun ProductSelectedItem(
    amount: BillItemChart,
    onClick: () -> Unit,
    onAction: (BillActions) -> Unit
) {

    val state = rememberSwipeToDismissBoxState()
    var showCalculator by remember { mutableStateOf(false) }

    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onAction(OnRemoveItem(amount))
            state.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    showCalculator.ifTrue {
        CalculatorBottomSheet(
            controller = CalculatorController(amount.quantity),
            onDismiss = { showCalculator = false }
        ) { onAction(OnUpdateItem(amount.copy(quantity = it))) }
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
            product = amount.product ?: ProductModel(price = amount.value),
            selection = amount.quantity,
            onQuantity = {
                when (it) {
                    ProductQuantityActionType.ADD -> onAction(OnAddItem(amount))
                    ProductQuantityActionType.SUBTRACT -> onAction(OnSubtractItem(amount))
                    ProductQuantityActionType.UPDATE -> showCalculator = true
                }
            },
        )
    }
}