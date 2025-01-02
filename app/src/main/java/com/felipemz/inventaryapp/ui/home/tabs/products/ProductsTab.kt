package com.felipemz.inventaryapp.ui.home.tabs.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.extensions.onColor

@Composable
internal fun InventoryTab(
    isInventory: Boolean,
    isFocusSearch: MutableState<Boolean>,
    categories: List<CategoryEntity>,
    products: List<ProductEntity>,
) {

    val text = remember { mutableStateOf(String()) }
    val chipSelected = remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {

        item {
            SearchTextField(
                isInventory = isInventory,
                isFocusSearch = isFocusSearch,
                text = text
            )
        }

        item {
            FilterChipCategories(
                modifier = Modifier.fillMaxWidth(),
                chipList = categories,
                chipSelected = chipSelected
            )
        }

        items(products) { item ->
            ProductItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(6.dp),
                product = item
            )
        }
    }
}

@Composable
private fun FilterChipCategories(
    modifier: Modifier,
    chipList: List<CategoryEntity>,
    chipSelected: MutableIntState
) = LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
    itemsIndexed(chipList) { index, item ->
        FilterChip(
            modifier = modifier.padding(
                start = if (item == chipList.first()) 12.dp else 0.dp,
                end = if (item == chipList.last()) 12.dp else 0.dp
            ),
            shape = CircleShape,
            border = if (index == chipSelected.intValue) {
                BorderStroke(
                    width = 2.dp,
                    color = if (item == chipList.first() || item == chipList.last()) MaterialTheme.colorScheme.primary
                    else colorResource(id = item.color)
                )
            } else null,
            colors = if (item == chipList.first() || item == chipList.last()) {
                FilterChipDefaults.filterChipColors().copy(
                    containerColor = Color.Gray.copy(alpha = 0.4f),
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary
                )
            } else FilterChipDefaults.filterChipColors().copy(
                containerColor = colorResource(id = item.color).copy(alpha = 0.15f),
                selectedContainerColor = colorResource(id = item.color),
                labelColor = MaterialTheme.colorScheme.onSurface,
                selectedLabelColor = colorResource(id = item.color).onColor()
            ),
            trailingIcon = {
                if (item == chipList.last()) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = null
                    )
                }
            },
            selected = index == chipSelected.intValue,
            onClick = { chipSelected.intValue = index },
            label = {
                Text(
                    text = item.name,
                    fontWeight = if (index == chipSelected.intValue) FontWeight.Black else FontWeight.Light
                )
            }
        )
    }
}

@Composable
private fun SearchTextField(
    isInventory: Boolean,
    isFocusSearch: MutableState<Boolean>,
    text: MutableState<String>,
) {

    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
            .onFocusChanged { isFocusSearch.value = it.isFocused },
        value = text.value,
        onValueChange = { text.value = it },
        placeholder = { Text("Buscar producto") },
        shape = CircleShape,
        leadingIcon = {
            LeadingSearch(isFocusSearch.value) {
                focusManager.clearFocus()
                text.value = String()
            }
        },
        trailingIcon = { TrailingSearch(isFocusSearch.value, text) },
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
    text: MutableState<String>
) = if (isFocusSearch) {
    Icon(
        modifier = Modifier
            .padding(end = 6.dp)
            .clip(CircleShape)
            .clickable { text.value = String() }
            .padding(4.dp),
        imageVector = Icons.Rounded.Clear,
        contentDescription = null
    )
} else Unit