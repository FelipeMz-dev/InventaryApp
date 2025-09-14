package com.felipemz.inventaryapp.ui.product_form.components.alert_dialog

import com.felipemz.inventaryapp.core.charts.CategoryUseChart
import com.felipemz.inventaryapp.core.charts.PackageUseChart
import com.felipemz.inventaryapp.domain.model.ProductModel

interface AlertDialogProductFormType {
    val message: String
    val canAction: Boolean
    val textAction: String

    data class DeleteCategory(
        val categoryId: Int,
        val categoryUsesError: List<CategoryUseChart>,
        override val message: String = "No se puede eliminar la categoría porque está siendo utilizada por los siguientes productos:",
        override val canAction: Boolean = categoryUsesError.none { !it.isChanged },
        override val textAction: String = "Eliminar categoría",
    ) : AlertDialogProductFormType

    data class DeleteProduct(
        val product: ProductModel,
        override val message: String = "Estás a punto de eliminar el siguiente producto:",
        override val canAction: Boolean = true,
        override val textAction: String = "Eliminar",
    ) : AlertDialogProductFormType

    data class DeleteProductPackaged(
        val packagesUsesError: List<PackageUseChart>,
        override val message: String = "No se puede eliminar el producto porque está siendo utilizado por los siguientes paquetes:",
        override val canAction: Boolean = packagesUsesError.none { !it.isChanged },
        override val textAction: String = "Eliminar",
    ) : AlertDialogProductFormType

    data class UpdateQuantityProductPackaged(
        val packagesUsesError: List<PackageUseChart>,
        override val message: String = "No se puede quitar la cantidad porque está siendo utilizada por los siguientes paquetes:",
        override val canAction: Boolean = packagesUsesError.none { !it.isChanged },
        override val textAction: String = "Guardar",
    ) : AlertDialogProductFormType
}