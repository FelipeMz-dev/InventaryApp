package com.felipemz.inventaryapp.ui.movements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.utils.CurrencyUtil
import com.felipemz.inventaryapp.ui.commons.HorizontalDotDivider
import com.felipemz.inventaryapp.ui.commons.actions.BillActions
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.ui.commons.ProductBillItemSelected
import com.felipemz.inventaryapp.ui.commons.actions.BillActions.OnUpdateItem
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorBottomSheet
import com.felipemz.inventaryapp.ui.commons.calculator.CalculatorController

@Composable
internal fun InvoiceColumn(
    subTotal: Int,
    discount: Int,
    total: Int,
    invoiceList: List<BillItemChart>,
    onAction: (BillActions) -> Unit,
) {

    var showCalculatorItem by remember { mutableStateOf<Int?>(null) }

    showCalculatorItem?.let { productId ->
        invoiceList.find { it.product?.id == productId }?.let { selection ->
            CalculatorBottomSheet(
                controller = CalculatorController(selection.quantity),
                onDismiss = { showCalculatorItem = null },
            ) { onAction(OnUpdateItem(selection.copy(quantity = it))) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 12.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentPadding = PaddingValues(bottom = 68.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(invoiceList) { item ->
            ProductBillItemSelected(
                item = item,
                onClick = { TODO("Show options") },
                onOpenCalculator = { showCalculatorItem = item.product?.id },
                onAction = onAction
            )
        }

        item {
            TotalInvoiceField(
                subTotal = subTotal,
                discount = discount,
                total = total
            )
        }
    }
}

@Composable
private fun TotalInvoiceField(
    subTotal: Int,
    discount: Int,
    total: Int
) {
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        HorizontalDotDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Subtotal:")
            Text(text = CurrencyUtil.formatPrice(subTotal))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Descuento:")
            Text(text = CurrencyUtil.formatPrice(discount))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total:")
            Text(text = CurrencyUtil.formatPrice(total))
        }
    }
}