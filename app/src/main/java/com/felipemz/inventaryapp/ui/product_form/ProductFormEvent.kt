package com.felipemz.inventaryapp.ui.product_form

import com.felipemz.inventaryapp.core.base.Event
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.ProductTypeImage
import com.felipemz.inventaryapp.ui.commons.actions.BillActions

sealed interface ProductFormEvent : Event {

    data class Init(
        val productId: Int?,
        val barcode: String?,
        val categoryId: Int?,
    ) : ProductFormEvent

    data object OnBack : ProductFormEvent

    data object CloseAlertDialog : ProductFormEvent

    data object OnProductSaved : ProductFormEvent

    data object OnTryDeleteProduct : ProductFormEvent

    data object OnProductDeleted : ProductFormEvent

    data class OnNameChanged(val name: String) : ProductFormEvent

    data class OnPriceChanged(val price: Int) : ProductFormEvent

    data class OnImageChanged(val image: ProductTypeImage) : ProductFormEvent

    data class OnCategoryChanged(val category: CategoryModel) : ProductFormEvent

    data class OnDescriptionChanged(val description: String) : ProductFormEvent

    data class OnCostChanged(val cost: Int?) : ProductFormEvent

    data class OnBarcodeChanged(val barcode: String?) : ProductFormEvent

    data class OnQuantityTypeChanged(val quantityType: QuantityType?) : ProductFormEvent

    data class OnQuantityChanged(val quantity: Int) : ProductFormEvent

    data class OnOpenProduct(val product: BillItemChart) : ProductFormEvent

    data class OnTogglePackage(val value: Boolean) : ProductFormEvent

    data class OnInsertOrUpdateCategory(val category: CategoryModel) : ProductFormEvent

    data class OnDeleteCategory(val categoryId: Int) : ProductFormEvent

    data class OnSortCategories(val from: CategoryModel, val to: CategoryModel) : ProductFormEvent

    data class GoToChangeCategory(val productId: Int, val categoryId: Int) : ProductFormEvent

    data class GoToChangePackage(val productId: Int) : ProductFormEvent

    data class SetCategoryToChange(val categoryId: Int) : ProductFormEvent

    data class SetPackageToChange(val packageId: Int) : ProductFormEvent

    data class SetChangedSuccessfulCategory(val productId: Int) : ProductFormEvent

    data class SetChangedSuccessfulPackage(val packageId: Int) : ProductFormEvent

    data class OnPackageAction(val action: BillActions) : ProductFormEvent

    data class OnSetNameFilter(val name: String?) : ProductFormEvent

    data class OnSetCategoryFilter(val category: CategoryModel?) : ProductFormEvent
}