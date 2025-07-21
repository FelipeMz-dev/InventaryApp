package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.ui.LocalProductList
import com.felipemz.inventaryapp.ui.commons.actions.BillActions
import com.felipemz.inventaryapp.ui.commons.actions.BillActions.OnInsertItem
import com.felipemz.inventaryapp.ui.commons.actions.BillActions.OnUpdateItem
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorBottomSheet
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorController
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductEvent
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductList
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsListBottomSheet(
    selection: List<BillItemChart>,
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
    val listState = rememberLazyListState()
    val showHeader by remember { derivedStateOf { sheetState.targetValue == SheetValue.Expanded } }
    var isOpenKeyboard by remember { mutableStateOf(false) }
    var showCalculatorItem by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(showHeader) {
        listState.requestScrollToItem(0)
        if (!showHeader && isOpenKeyboard) {
            sheetState.expand()
        }
        if (!showHeader) {
            onSetCategoryFilter(null)
            onSetNameFilter(null)
        }
    }

    showCalculatorItem?.let { productId ->
        selection.find { it.product?.id == productId }?.let { selection ->
            CalculatorBottomSheet(
                controller = CalculatorController(selection.quantity),
                onDismiss = { showCalculatorItem = null },
            ) { onAction(OnUpdateItem(selection.copy(quantity = it))) }
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

        LazyColumn(
            overscrollEffect = null,
            state = listState
        ) {
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

                val selected = selection.find { it.product?.id == product.id }

                selected?.let {
                    ProductBillItemSelected(
                        item = it,
                        onClick = { onAction(OnInsertItem(it)) },
                        onOpenCalculator = { showCalculatorItem = product.id },
                        onAction = onAction
                    )
                } ?: ProductItem(
                    modifier = Modifier.fillMaxWidth(),
                    product = product,
                ) { onAction(OnInsertItem(BillItemChart(product = product))) }
            }
        }

        if (showHeader || isOpenKeyboard) {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}