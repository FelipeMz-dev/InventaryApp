package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.ui.LocalProductList
import com.felipemz.inventaryapp.ui.commons.actions.BillActions
import com.felipemz.inventaryapp.ui.commons.actions.BillActions.*
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorBottomSheet
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorController
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductEvent
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductList
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductQuantityActionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsListBottomSheet(
    selected: List<BillItemChart>,
    categories: List<CategoryModel>,
    initialCategory: CategoryModel? = null,
    emptyMessage: String = "No hay productos disponibles",
    onSetCategoryFilter: (CategoryModel?) -> Unit,
    onSetNameFilter: (String?) -> Unit,
    onDismiss: () -> Unit,
    onAction: (BillActions) -> Unit,
) {

    val products = LocalProductList.current
    val sheetState = rememberModalBottomSheetState()
    val showHeader by remember { derivedStateOf { sheetState.targetValue == SheetValue.Expanded } }

    var isOpenKeyboard by remember { mutableStateOf(false) }

    LaunchedEffect(showHeader) {
        if (!showHeader && isOpenKeyboard) {
            sheetState.expand()
        }
        if (!showHeader) {
            onSetCategoryFilter(null)
            onSetNameFilter(null)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {

        if (products.isEmpty() && !showHeader) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = emptyMessage,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            return@ModalBottomSheet
        }

        LazyColumn {
            if (showHeader || isOpenKeyboard) {
                item {
                    HeaderProductList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        categories = categories,
                        categorySelected = initialCategory,
                        isFocusSearch = isOpenKeyboard,
                        headerProductEventHandler = { event ->
                            when (event) {
                                is HeaderProductEvent.OnFocusSearch -> isOpenKeyboard = event.isFocus
                                is HeaderProductEvent.OnChangeSearchText -> onSetNameFilter(event.text)
                                is HeaderProductEvent.OnChangeCategory -> onSetCategoryFilter(event.category)
                            }
                        }
                    )
                }
            }

            products.ifEmpty {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "No hay resultados",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(
                items = products,
                key = { it.id },
            ) { product ->

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

        if (showHeader || isOpenKeyboard) {
            Box(modifier = Modifier.fillMaxSize())
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