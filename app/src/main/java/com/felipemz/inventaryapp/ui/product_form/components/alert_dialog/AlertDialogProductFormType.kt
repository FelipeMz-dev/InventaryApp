package com.felipemz.inventaryapp.ui.product_form.components.alert_dialog

import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.core.charts.CategoryUseChart

interface AlertDialogProductFormType {
    val message: String
    val canAccept: Boolean

    data class DeleteCategory(
        val categoryId: Int,
        val categoryUsesError: List<CategoryUseChart>
    ) : AlertDialogProductFormType {
        override val message: String = "No se puede eliminar la categoría porque está siendo utilizada por los siguientes productos:"
        override val canAccept: Boolean = categoryUsesError.none { !it.isChanged }
    }

    data class DeleteProduct(
        val product: ProductModel
    ) : AlertDialogProductFormType {
        override val message: String = "Estás a punto de eliminar el siguiente producto:"
        override val canAccept: Boolean = true
    }
}