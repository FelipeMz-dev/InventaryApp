package com.felipemz.inventaryapp.ui.product_form.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.ui.commons.OutlinedPriceField

@Composable
internal fun PriceField(
    modifier: Modifier,
    value: Int,
    onChange: (Int) -> Unit
) {
    CommonFormField(
        modifier = modifier,
        title = stringResource(R.string.copy_price_dots)
    ) {
        OutlinedPriceField(
            modifier = modifier,
            value = value,
            onChange = onChange
        )
    }
}