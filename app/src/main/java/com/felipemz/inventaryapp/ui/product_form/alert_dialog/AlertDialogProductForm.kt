package com.felipemz.inventaryapp.ui.product_form.alert_dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import com.felipemz.inventaryapp.ui.commons.CommonAlertDialog
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import com.felipemz.inventaryapp.ui.product_form.CategoryUseChart
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.CloseAlertDialog
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.GoToChangeCategory
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnDeleteCategory
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnProductDeleted
import com.felipemz.inventaryapp.ui.product_form.alert_dialog.AlertDialogProductFormType.*

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
                        Text(
                            text = "${dialog.product.id} ${dialog.product.name}",
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis
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
                    text = it.usedId.toString().padStart(
                        length = 6,
                        padChar = '0'
                    ),
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