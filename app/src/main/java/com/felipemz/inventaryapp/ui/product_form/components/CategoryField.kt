package com.felipemz.inventaryapp.ui.product_form.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.extensions.onColor

@Composable
internal fun CategoryField(
    modifier: Modifier,
    category: CategoryEntity?,
    categories: List<CategoryEntity>,
    onSelect: (CategoryEntity) -> Unit,
) {

    var showDropCategory by remember { mutableStateOf(false) }

    CommonFormField(
        modifier = modifier,
        title = stringResource(R.string.copy_category_dots)
    ) {

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showDropCategory = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = category?.let {
                    colorResource(id = it.color)
                } ?: MaterialTheme.colorScheme.secondaryContainer,
                contentColor = category?.let {
                    colorResource(id = it.color).onColor()
                } ?: MaterialTheme.colorScheme.onSurface
            )
        ) {

            Text(
                text = category?.name ?: stringResource(R.string.copy_select),
                maxLines = 1
            )

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null
            )
        }

        CategoriesDropDownMenu(
            showDropCategory = showDropCategory,
            categories = categories,
            onDismiss = { showDropCategory = false },
            onSelect = {
                showDropCategory = false
                onSelect(it)
            }
        )
    }
}

@Composable
private fun CategoriesDropDownMenu(
    showDropCategory: Boolean,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSelect: (CategoryEntity) -> Unit
) = DropdownMenu(
    expanded = showDropCategory,
    onDismissRequest = { onDismiss() },
    shape = RoundedCornerShape(8.dp),
    properties = PopupProperties(
        focusable = true,
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    )
) {
    categories.forEach { category ->
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                color = colorResource(id = category.color),
                                shape = CircleShape
                            )
                            .size(24.dp)
                    )

                    Text(text = category.name)
                }
            },
            onClick = { onSelect(category) }
        )
    }
}
