package com.felipemz.inventaryapp.ui.product_form.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.ui.commons.CommonFormField
import com.felipemz.inventaryapp.ui.commons.OutlinedPriceField

@Composable
internal fun CostField(
    modifier: Modifier,
    value: Int?,
    isEnable: Boolean = true,
    onChange: (Int?) -> Unit,
    onOpen: suspend () -> Unit
) {
    CommonFormField(
        modifier = modifier,
        title = "Costo:",
        isMandatory = false,
        visible = value.isNotNull(),
        concealable = true,
        onOpen = onOpen,
        thumbContent = {
            Switch(
                checked = value.isNotNull(),
                enabled = isEnable,
                onCheckedChange = { state ->
                    onChange(if (value.isNull()) 0 else null)
                }
            )
        }
    ) {
        OutlinedPriceField(
            modifier = Modifier.fillMaxWidth(),
            value = value ?: 0,
            isEnable = isEnable,
            onChange = onChange
        )
    }
}