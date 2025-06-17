package com.felipemz.inventaryapp.ui.movements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.domain.model.ProductSelectionChart
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorBottomSheet
import com.felipemz.inventaryapp.ui.commons.ProductsAddBottomSheet
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorController
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.IncrementCalculatorId
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnBack
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnChangeDiscount
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnDeleteMovement
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnSaveMovement
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnSelectProduct
import com.felipemz.inventaryapp.ui.movements.components.BottomBarMovements
import com.felipemz.inventaryapp.ui.movements.components.IdMovementField
import com.felipemz.inventaryapp.ui.movements.components.actions.FABMovements
import com.felipemz.inventaryapp.ui.movements.components.InvoiceColumn
import com.felipemz.inventaryapp.ui.movements.components.TopBarMovements
import com.felipemz.inventaryapp.ui.movements.components.actions.MovementsActions
import com.felipemz.inventaryapp.ui.product_form.components.QuantityChangeBottomSheet

@Composable
internal fun MovementsScreen(
    state: MovementsState,
    eventHandler: (MovementsEvent) -> Unit,
) {

    var showProductsPopup by remember { mutableStateOf(false) }
    var showCalculatorPopup by remember { mutableStateOf(false) }
    var showDiscountPopup by remember { mutableStateOf(false) }
    var showProductQuantityPopup by remember { mutableStateOf<ProductSelectionChart?>(null) }

    val fabActions: (MovementsActions) -> Unit = { action ->
        when (action) {
            MovementsActions.CALCULATOR -> showCalculatorPopup = true
            MovementsActions.SCANNER -> showDiscountPopup = true
            MovementsActions.DETAILS -> {}
            MovementsActions.NAVIGATE -> {}
        }
    }

    when {
        showProductsPopup -> ProductsAddBottomSheet(
            productList = state.productList,
            selected = state.selectedProducts,
            onQuantity = { showProductQuantityPopup = it },
            onDismiss = { showProductsPopup = false },
            onSelect = { eventHandler(OnSelectProduct(it)) }
        )
        showDiscountPopup -> QuantityChangeBottomSheet(
            currentQuantity = state.discount,
            quantityType = QuantityType.UNIT,
            onDismiss = { showDiscountPopup = false },
            onSelect = {
                eventHandler(OnChangeDiscount(it))
                showDiscountPopup = false
            }
        )
        showCalculatorPopup -> CalculatorBottomSheet(
            controller = CalculatorController(0),
            onDismiss = { showCalculatorPopup = false },
            onSelect = {
                eventHandler(IncrementCalculatorId)
                eventHandler(
                    OnSelectProduct(
                        ProductSelectionChart(
                            price = it,
                            quantity = 1
                        )
                    )
                )
            }
        )
    }

    showProductQuantityPopup?.let { product ->
        QuantityChangeBottomSheet(
            currentQuantity = product.quantity,
            quantityType = product.product?.quantityModel?.type ?: QuantityType.UNIT,
            onDismiss = { showProductQuantityPopup = null },
            onSelect = {
                eventHandler(OnSelectProduct(product.copy(quantity = it)))
                showProductQuantityPopup = null
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopBarMovements(
                onBack = { eventHandler(OnBack) },
                title = state.movementState.title
            )
        },
        bottomBar = {
            state.movementState.typeAction?.let {
                BottomBarMovements(
                    text = it,
                    enable = state.movementState.isDone(),
                    action = { eventHandler(OnSaveMovement) }
                )
            }
        },
        floatingActionButton = {
            FABMovements(
                onAction = { fabActions(it) },
                onOpenProducts = { showProductsPopup = true }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Text(
                text = "${state.movementState.typeName} #${state.movementNumber} - ${state.movementDate}",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelMedium,
            )

            IdMovementField(
                modifier = Modifier.fillMaxWidth(),
                idMovement = state.movementId,
                movementType = state.movementState,
            ) { eventHandler(OnDeleteMovement) }

            InvoiceColumn(
                subTotal = state.subTotal,
                discount = state.discount,
                total = state.total,
                selectedProducts = state.selectedProducts,
                onQuantityProduct = { showProductQuantityPopup = it },
                onSelect = { eventHandler(OnSelectProduct(it)) }
            )
        }
    }
}