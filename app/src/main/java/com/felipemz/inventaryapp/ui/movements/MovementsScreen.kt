package com.felipemz.inventaryapp.ui.movements

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.core.utils.CurrencyUtil
import com.felipemz.inventaryapp.ui.commons.BarcodeScannerDialog
import com.felipemz.inventaryapp.ui.commons.CommonCustomDialog
import com.felipemz.inventaryapp.ui.commons.actions.BillActions
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorBottomSheet
import com.felipemz.inventaryapp.ui.commons.ProductsListBottomSheet
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorController
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.*
import com.felipemz.inventaryapp.ui.movements.components.BottomBarMovements
import com.felipemz.inventaryapp.ui.movements.components.IdMovementField
import com.felipemz.inventaryapp.ui.movements.components.actions.FABMovements
import com.felipemz.inventaryapp.ui.movements.components.InvoiceColumn
import com.felipemz.inventaryapp.ui.movements.components.TopBarMovements
import com.felipemz.inventaryapp.ui.movements.components.actions.MovementsActions
import java.util.Locale

@Composable
internal fun MovementsScreen(
    state: MovementsState,
    eventHandler: (MovementsEvent) -> Unit,
) {

    var showProductsPopup by remember { mutableStateOf(false) }
    var showCalculatorPopup by remember { mutableStateOf(false) }
    var showScannerDialog by remember { mutableStateOf(false) }

    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        matches?.firstOrNull()?.let {
            val value = CurrencyUtil.getValue(it)
            if (value > 0) eventHandler(
                OnInvoiceAction(
                    BillActions.OnInsertItem(BillItemChart(value = value))
                )
            )
        }
    }

    val fabActions: (MovementsActions) -> Unit = { action ->
        when (action) {
            MovementsActions.CALCULATOR -> showCalculatorPopup = true
            MovementsActions.SCANNER -> showScannerDialog = true
            MovementsActions.MICROPHONE -> launcher.launch(intent)
            MovementsActions.NAVIGATE -> {}
        }
    }

    when {
        showProductsPopup -> ProductsListBottomSheet(
            selection = state.billList.filter { it.product.isNotNull() },
            categories = state.categories,
            onSetNameFilter = { eventHandler(OnSetNameFilter(it)) },
            onSetCategoryFilter = { eventHandler(OnSetCategoryFilter(it)) },
            onDismiss = { showProductsPopup = false },
            onAction = { eventHandler(OnInvoiceAction(it)) }
        )
        showCalculatorPopup -> CalculatorBottomSheet(
            onDismiss = { showCalculatorPopup = false },
            onSelect = { value ->
                eventHandler(
                    OnInvoiceAction(
                        BillActions.OnInsertItem(BillItemChart(value = value))
                    )
                )
            }
        )
        showScannerDialog -> BarcodeScannerDialog(
            onDismiss = { showScannerDialog = false },
        ) { eventHandler(OnSelectProductFromBarcode(it)) }
    }

    state.errorBarcode?.let {
        CommonCustomDialog(
            title = "Barcode not found",
            onDismiss = { eventHandler(OnClearBarcodeError) }
        ) {
            Text(
                text = "The barcode '$it' does not match any product in the inventory.",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        eventHandler(OnExecuteAction(MovementsAction.CreateProductFromBarcode(it)))
                    }
                ) {
                    Text(text = "Create new")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        eventHandler(OnClearBarcodeError)
                        showScannerDialog = true
                    }
                ) {
                    Text(text = "Scan Again")
                }
            }
        }
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
                text = formattedMovementTitle(
                    typeName = state.movementState.typeName,
                    movementNumber = state.movementNumber,
                    movementDate = state.movementDate
                ),
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
                invoiceList = state.billList,
                onAction = { eventHandler(OnInvoiceAction(it)) },
            )
        }
    }
}

@Composable
private fun formattedMovementTitle(
    typeName: String,
    movementNumber: Int,
    movementDate: String,
): String = "$typeName #$movementNumber - $movementDate"