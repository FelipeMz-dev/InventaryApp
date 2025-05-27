package com.felipemz.inventaryapp.ui.movements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.domain.model.ProductSelectionChart
import com.felipemz.inventaryapp.core.enums.MovementStateType
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.ui.commons.CalculatorBottomSheet
import com.felipemz.inventaryapp.ui.commons.ProductsAddBottomSheet
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.IncrementCalculatorId
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnBack
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnChangeDiscount
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnDeleteMovement
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnSaveMovement
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnSelectProduct
import com.felipemz.inventaryapp.ui.movements.components.InvoiceList
import com.felipemz.inventaryapp.ui.product_form.components.QuantityChangeBottomSheet
import com.felipemz.inventaryapp.ui.product_form.components.getIdString

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
            MovementsActions.DISCOUNT -> showDiscountPopup = true
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
            currentQuantity = 0,
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

            InvoiceList(
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

@Composable
internal fun IdMovementField(
    modifier: Modifier,
    idMovement: Int?,
    movementType: MovementStateType,
    action: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = getIdString(idMovement),
            fontWeight = FontWeight.Bold,
        )

        IconButton(onClick = action) {
            movementType.isDone().ifTrue {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBarMovements(
    onBack: () -> Unit,
    title: String,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun BottomBarMovements(
    text: String,
    enable: Boolean,
    action: () -> Unit,
) {
    BottomAppBar {
        Button(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            onClick = action,
            enabled = enable
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FABMovements(
    onAction: (MovementsActions) -> Unit,
    onOpenProducts: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = CircleShape
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MovementsActions.entries.forEach {
                IconButton(onClick = { onAction(it) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = it.resId),
                        contentDescription = null
                    )
                }
            }
        }

        FloatingActionButton(onClick = onOpenProducts) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_product_add),
                contentDescription = null
            )
        }
    }
}

enum class MovementsActions(val resId: Int) {
    CALCULATOR(R.drawable.ic_calculator),
    DISCOUNT(R.drawable.ic_discount),
    DETAILS(R.drawable.ic_details),
    NAVIGATE(R.drawable.ic_navigate), ;
}