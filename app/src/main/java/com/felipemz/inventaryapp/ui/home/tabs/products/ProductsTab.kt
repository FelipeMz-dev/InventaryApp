package com.felipemz.inventaryapp.ui.home.tabs.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.model.CategoryModel
import com.felipemz.inventaryapp.model.ProductModel
import com.felipemz.inventaryapp.core.extensions.onColor
import com.felipemz.inventaryapp.ui.commons.FilterChipRow
import com.felipemz.inventaryapp.ui.home.HomeEvent

@Composable
internal fun InventoryTab(
    categories: List<CategoryModel>,
    categorySelected: CategoryModel?,
    isInventory: Boolean,
    isFocusSearch: Boolean,
    products: List<ProductModel>,
    eventHandler: (HomeEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {

        item {
            SearchTextField(
                isInventory = isInventory,
                isFocusSearch = isFocusSearch,
                onTextChange = { eventHandler(HomeEvent.OnChangeSearchText(it)) },
                onFocusChange = { eventHandler(HomeEvent.OnFocusSearch(it)) }
            )
        }

        item {
            FilterChipCategories(
                modifier = Modifier.fillMaxWidth(),
                chipList = categories,
                chipSelected = categorySelected,
            ) { eventHandler(HomeEvent.OnCategorySelected(it)) }
        }

        items(products) { item ->
            ProductItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { eventHandler(HomeEvent.OnOpenProduct(item)) }
                    .padding(horizontal = 6.dp),
                product = item
            )
        }
    }
}

@Composable
private fun FilterChipCategories(
    modifier: Modifier,
    chipList: List<CategoryModel>,
    chipSelected: CategoryModel?,
    onSelectChip: (CategoryModel?) -> Unit,
) = FilterChipRow(
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
                containerColor = Color.Gray.copy(alpha = 0.4f),
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary
        )
    },
    text = { it?.name ?: "Todos" },
    chipList = listOf(null).plus(chipList),
    chipSelected = chipSelected,
    onSelectChip = onSelectChip
)

@Composable
private fun SearchTextField(
    isInventory: Boolean,
    isFocusSearch: Boolean,
    onFocusChange: (Boolean) -> Unit,
    onTextChange: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(String()) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
            .onFocusChanged { onFocusChange(it.isFocused) },
        value = text,
        onValueChange = {
            text = it
            onTextChange(it)
        },
        placeholder = { Text(stringResource(R.string.copy_product_search)) },
        shape = CircleShape,
        leadingIcon = {
            LeadingSearch(isFocusSearch) {
                focusManager.clearFocus()
                text = String()
                onTextChange(String())
            }
        },
        trailingIcon = {
            TrailingSearch(isFocusSearch) {
                text = String()
                onTextChange(String())
            }
        },
        singleLine = true,
        enabled = isInventory,
        colors = TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun LeadingSearch(
    isFocusSearch: Boolean,
    onBack: () -> Unit
) = Row(verticalAlignment = Alignment.CenterVertically) {

    if (isFocusSearch) {
        Icon(
            modifier = Modifier
                .padding(start = 6.dp)
                .clip(CircleShape)
                .clickable { onBack() }
                .padding(4.dp),
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null
        )
    }

    Text("🔍")
}

@Composable
private fun TrailingSearch(
    isFocusSearch: Boolean,
    onClean: () -> Unit,
) = if (isFocusSearch) {
    Icon(
        modifier = Modifier
            .padding(end = 6.dp)
            .clip(CircleShape)
            .clickable { onClean() }
            .padding(4.dp),
        imageVector = Icons.Rounded.Clear,
        contentDescription = null
    )
} else Unit