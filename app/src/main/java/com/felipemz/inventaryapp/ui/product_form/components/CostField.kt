package com.felipemz.inventaryapp.ui.product_form.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.felipemz.inventaryapp.ui.commons.OutlinedPriceField

@Composable
internal fun CostField(
    modifier: Modifier,
    value: Int,
    onChange: (Int) -> Unit,
    onOpen: suspend () -> Unit
) {
    CommonFormField(
        modifier = modifier,
        title = "Costo:",
        isMandatory = false,
        visible = false,
        concealable = true,
        onOpen = onOpen
    ) {
       OutlinedPriceField(
           modifier = Modifier.fillMaxWidth(),
           value = value,
           onChange = onChange
       )
    }
}