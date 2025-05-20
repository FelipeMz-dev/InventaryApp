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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.model.ProductQuantityEntity
import com.felipemz.inventaryapp.core.utils.PriceUtil
import com.felipemz.inventaryapp.ui.commons.HorizontalDotDivider
import com.felipemz.inventaryapp.ui.product_form.components.ProductPackageItem

@Composable
internal fun InvoiceList(
    subTotal: Int,
    discount: Int,
    total: Int,
    selectedProducts: List<ProductQuantityEntity>,
    onQuantityProduct: (ProductQuantityEntity) -> Unit,
    onSelect: (ProductQuantityEntity) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 12.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentPadding = PaddingValues(bottom = 68.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(selectedProducts) { product ->
            ProductPackageItem(
                product = product,
                onClick = { },
                onChangeSelection = { onSelect(product.copy(quantity = it)) },
                onQuantity = { onQuantityProduct(product) },
                onDelete = { onSelect(product.copy(quantity = 0)) }
            )
        }

        item {

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
                    Text(text = PriceUtil.formatPrice(subTotal))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Descuento:")
                    Text(text = PriceUtil.formatPrice(discount))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total:")
                    Text(text = PriceUtil.formatPrice(total))
                }
            }
        }
    }
}