package com.felipemz.inventaryapp.ui.product_form.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.ifFalse

@Composable
internal fun CommonTrailingIcon(
    isTextEmpty: Boolean,
    onEdit: (String) -> Unit,
) {
    IconButton(
        onClick = { isTextEmpty.ifFalse { onEdit(String()) } }
    ) {
        Icon(
            imageVector = if (!isTextEmpty) Icons.Rounded.Clear
            else ImageVector.vectorResource(id = R.drawable.ic_mic),
            tint = MaterialTheme.colorScheme.outline,
            contentDescription = null
        )
    }
}