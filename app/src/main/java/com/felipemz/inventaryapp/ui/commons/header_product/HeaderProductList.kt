package com.felipemz.inventaryapp.ui.commons.header_product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.onColor
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.ui.commons.CommonTrailingIcon
import com.felipemz.inventaryapp.ui.commons.FilterChipRow
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductEvent.OnChangeCategory
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductEvent.OnChangeSearchText
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductEvent.OnFocusSearch

@Composable
fun HeaderProductList(
    modifier: Modifier,
    isInventory: Boolean = true,
    isFocusSearch: Boolean = true,
    categories: List<CategoryModel>,
    categorySelected: CategoryModel?,
    headerProductEventHandler: (HeaderProductEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SearchTextField(
            isEnable = isInventory,
            isFocusSearch = isFocusSearch,
            onTextChange = { headerProductEventHandler(OnChangeSearchText(it)) },
            onFocusChange = { headerProductEventHandler(OnFocusSearch(it)) }
        )

        FilterChipCategories(
            modifier = Modifier.Companion.fillMaxWidth(),
            chipList = categories,
            initialValue = categorySelected,
            onSelectChip = { headerProductEventHandler(OnChangeCategory(it)) }
        )
    }
}

@Composable
private fun FilterChipCategories(
    modifier: Modifier,
    chipList: List<CategoryModel>,
    initialValue: CategoryModel?,
    onSelectChip: (CategoryModel?) -> Unit,
) {

    var selected by remember { mutableStateOf(initialValue) }

    FilterChipRow(
        modifier = modifier,
        colors = {
            it?.let {
                FilterChipDefaults.filterChipColors().copy(
                    containerColor = colorResource(id = it.color).copy(alpha = 0.15f),
                    selectedContainerColor = colorResource(id = it.color),
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    selectedLabelColor = colorResource(id = it.color).onColor()
                )
            } ?: FilterChipDefaults.filterChipColors().copy(
                containerColor = Color.Companion.Gray.copy(alpha = 0.4f),
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        text = { it?.name ?: "Todos" },
        chipList = listOf(null).plus(chipList),
        chipSelected = selected,
        onSelectChip = {
            selected = it
            onSelectChip(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField(
    isEnable: Boolean = true,
    isFocusSearch: Boolean,
    onFocusChange: (Boolean) -> Unit,
    onTextChange: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(String()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LeadingSearchIcon(isFocusSearch) {
            focusManager.clearFocus()
            text = String()
            onTextChange(String())
        }

        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .onFocusChanged { onFocusChange(it.isFocused) },
            value = text,
            onValueChange = {
                text = it
                onTextChange(it)
            },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            enabled = isEnable,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text(
                        text = stringResource(R.string.copy_product_search),
                        color = Color.Companion.Gray
                    )
                }
                innerTextField()
            }
        )

        CommonTrailingIcon(text.isEmpty()) {
            text = it
            onTextChange(it)
        }
    }
}

@Composable
private fun LeadingSearchIcon(
    isFocusSearch: Boolean,
    onBack: () -> Unit
) {
    IconButton(
        onClick = onBack,
        enabled = isFocusSearch
    ) {
        Icon(
            imageVector = if (isFocusSearch) Icons.Rounded.ArrowBack else Icons.Default.Search,
            tint = MaterialTheme.colorScheme.outline,
            contentDescription = null
        )
    }
}