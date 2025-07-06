package com.felipemz.inventaryapp.ui.product_form.components.alert_dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.felipemz.inventaryapp.core.extensions.toIdString
import com.felipemz.inventaryapp.ui.commons.CommonAlertDialog
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductItem
import com.felipemz.inventaryapp.core.charts.CategoryUseChart
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.CloseAlertDialog
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.GoToChangeCategory
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnDeleteCategory
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnProductDeleted
import com.felipemz.inventaryapp.ui.product_form.components.alert_dialog.AlertDialogProductFormType.*

@Composable
fun AlertDialogProductForm(
    alertDialog: AlertDialogProductFormType?,
    eventHandler: (ProductFormEvent) -> Unit
) {
    alertDialog?.let { dialog ->
        CommonAlertDialog(
            message = dialog.message,
            body = {
                when(dialog) {
                    is DeleteCategory -> BodyDialogDeleteCategory(
                        categoryUsesError = dialog.categoryUsesError,
                        action = { eventHandler(GoToChangeCategory(it.usedId, dialog.categoryId)) }
                    )
                    is DeleteProduct -> {
                        ProductItem(
                            modifier = Modifier.fillMaxWidth(),
                            product = dialog.product,
                            isSmall = true
                        )
                    }
                }
            },
            canAccept = dialog.canAccept,
            onDismiss = { eventHandler(CloseAlertDialog) },
            onAccept = {
                eventHandler(CloseAlertDialog)
                when (dialog) {
                    is DeleteProduct -> eventHandler(OnProductDeleted)
                    is DeleteCategory -> eventHandler(OnDeleteCategory(dialog.categoryId))
                }
            }
        )
    }
}

@Composable
private fun BodyDialogDeleteCategory(
    categoryUsesError: List<CategoryUseChart>,
    action: (CategoryUseChart) -> Unit
) {
    Column {
        categoryUsesError.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButtonUnderline(
                    text = it.usedId.toIdString(),
                    isEnabled = !it.isChanged
                ) { action(it) }

                Text(
                    text = it.usesName,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.outline,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}