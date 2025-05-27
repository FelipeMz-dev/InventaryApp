package com.felipemz.inventaryapp.ui.product_form.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.CategoryColor
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.extensions.onColor
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.ui.commons.CommonFormField
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline

@Composable
internal fun CategoryField(
    modifier: Modifier,
    category: CategoryModel?,
    categories: List<CategoryModel>,
    categoryIdToChange: Int? = null,
    onInsertOrUpdate: (CategoryModel) -> Unit,
    onDelete: (CategoryModel) -> Unit,
    onSelect: (CategoryModel) -> Unit,
) {

    var showDropCategory by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<CategoryModel?>(null) }

    fun onEventCategory(event: CategoryItemEvent) = when (event) {
        is CategoryItemEvent.OnDismiss -> {
            showDropCategory = false
        }
        is CategoryItemEvent.OnCreate -> {
            showDropCategory = false
            categoryToEdit = CategoryModel()
        }
        is CategoryItemEvent.OnDelete -> {
            onDelete(event.category)
        }
        is CategoryItemEvent.OnEdit -> {
            showDropCategory = false
            categoryToEdit = event.category
        }
        is CategoryItemEvent.OnSelect -> {
            showDropCategory = false
            onSelect(event.category)
        }
    }

    CommonFormField(
        modifier = modifier,
        title = stringResource(R.string.copy_category_dots)
    ) {

        ButtonCategories(
            modifier = Modifier.fillMaxWidth(),
            category = category
        ) { showDropCategory = true }

        CategoriesDropDownMenu(
            showDropCategory = showDropCategory,
            categories = categoryIdToChange?.let { idToChange ->
                categories.filterNot { it.id == idToChange }
            } ?: categories,
            canEdit = categoryIdToChange.isNull(),
            onEvent = { onEventCategory(it) }
        )
    }

    categoryToEdit?.let { category ->
        CategoryEditorDialog(
            category = category,
            enableColors = CategoryColor.entries.filterNot { color ->
                categories.any { it.color == color.colorId && it.id != category.id }
            },
            onDismiss = { categoryToEdit = null }
        ) {
            onInsertOrUpdate(it)
            categoryToEdit = null
        }
    }
}

@Composable
private fun ButtonCategories(
    modifier: Modifier,
    category: CategoryModel?,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
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
}

sealed interface CategoryItemEvent {
    object OnDismiss : CategoryItemEvent
    object OnCreate : CategoryItemEvent
    data class OnDelete(val category: CategoryModel) : CategoryItemEvent
    data class OnEdit(val category: CategoryModel) : CategoryItemEvent
    data class OnSelect(val category: CategoryModel) : CategoryItemEvent
}

@Composable
private fun CategoriesDropDownMenu(
    showDropCategory: Boolean,
    categories: List<CategoryModel>,
    canEdit: Boolean,
    onEvent: (CategoryItemEvent) -> Unit,
) {
    DropdownMenu(
        expanded = showDropCategory,
        onDismissRequest = { onEvent(CategoryItemEvent.OnDismiss) },
        shape = RoundedCornerShape(8.dp),
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        categories.forEach { category ->
            DropdownCategoryItem(
                category = category,
                canEdit = canEdit,
                onDelete = { onEvent(CategoryItemEvent.OnDelete(category)) },
                onEdit = { onEvent(CategoryItemEvent.OnEdit(category)) },
                onSelect = { onEvent(CategoryItemEvent.OnSelect(category)) }
            )
        }

        DropdownMenuItem(
            text = {
                TextButtonUnderline(
                    text = "Crear categoría",
                    onClick = { onEvent(CategoryItemEvent.OnCreate) }
                )
            },
            onClick = { onEvent(CategoryItemEvent.OnCreate) }
        )
    }
}

@Composable
private fun DropdownCategoryItem(
    category: CategoryModel,
    canEdit: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onSelect: () -> Unit
) {

    val state = rememberSwipeToDismissBoxState()

    LaunchedEffect(state.currentValue) {
        when (state.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> {
                onDelete()
                state.snapTo(SwipeToDismissBoxValue.Settled)
            }
            SwipeToDismissBoxValue.StartToEnd -> {
                onEdit()
            }
            else -> Unit
        }
    }

    DropdownMenuItem(
        text = {
            SwipeToDismissBox(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                gesturesEnabled = canEdit,
                backgroundContent = {

                    if (state.dismissDirection == SwipeToDismissBoxValue.StartToEnd) Icon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.Start)
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Default.Edit,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null
                    )

                    if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) Icon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Default.Delete,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = null
                    )
                }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = colorResource(category.color),
                            shape = CircleShape
                        )
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(
                            vertical = 4.dp,
                            horizontal = 12.dp
                        ),
                    text = category.name,
                    textAlign = TextAlign.Center,
                    color = colorResource(category.color).onColor()
                )
            }
        },
        onClick = onSelect
    )
}