package com.felipemz.inventaryapp.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.ui.commons.HorizontalDotDivider
import com.felipemz.inventaryapp.ui.commons.CommonCustomDialog

@Composable
internal fun ProductsSortDialog(
    productOrderSelected: ProductsOrderBy,
    isProductOrderInverted: Boolean,
    onClose: (ProductsOrderBy, Boolean) -> Unit
) {

    var orderBy by remember { mutableStateOf(productOrderSelected) }
    var isOrderInverted by remember { mutableStateOf(isProductOrderInverted) }

    CommonCustomDialog(
        title = stringResource(R.string.copy_order_by),
        onDismiss = { onClose(orderBy, isOrderInverted) },
    ) {
        Column {
            ProductsOrderBy.entries.forEach { order ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { orderBy = order },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = order.text,
                        modifier = Modifier.padding(8.dp)
                    )

                    Checkbox(
                        checked = orderBy == order,
                        onCheckedChange = { orderBy = order }
                    )
                }
            }
        }

        HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isOrderInverted = !isOrderInverted },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.copy_inverter),
                modifier = Modifier.padding(8.dp)
            )

            Switch(
                checked = isOrderInverted,
                onCheckedChange = { isOrderInverted = it }
            )
        }
    }
}