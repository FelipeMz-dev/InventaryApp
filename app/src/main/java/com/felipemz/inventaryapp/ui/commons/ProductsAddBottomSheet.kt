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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.ui.commons.actions.BillActions.*
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorBottomSheet
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorController
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductQuantityActionType
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.ui.commons.actions.BillActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsListBottomSheet(
    productList: List<ProductModel>,
    selected: List<BillItemChart>,
    onDismiss: () -> Unit,
    onAction: (BillActions) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn {
            items(productList) { product ->

                val selection = remember(selected) {
                    selected.find { it.product?.id == product.id }
                }

                var showCalculator by remember { mutableStateOf(false) }

                showCalculator.ifTrue {
                    selection?.let { amount ->
                        CalculatorBottomSheet(
                            controller = CalculatorController(amount.quantity),
                            onDismiss = { showCalculator = false }
                        ) { onAction(OnUpdateItem(amount.copy(quantity = it))) }
                    }
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
                                    ProductQuantityActionType.UPDATE -> showCalculator = true
                                }
                            }
                        }
                    ) { onAction(OnInsertItem(BillItemChart(product = product))) }
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