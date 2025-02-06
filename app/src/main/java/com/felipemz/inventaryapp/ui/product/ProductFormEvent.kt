package com.felipemz.inventaryapp.ui.product

import com.felipemz.inventaryapp.core.base.Event
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

sealed interface ProductFormEvent: Event {

    data class Init(val productId: Int): ProductFormEvent

    data object OnBack: ProductFormEvent

    data object OnProductSaved: ProductFormEvent

    data object OnProductDeleted: ProductFormEvent

    data class OnNameChanged(val name: String): ProductFormEvent

    data class OnDescriptionChanged(val description: String): ProductFormEvent

    data class OnPriceChanged(val price: Int): ProductFormEvent

    data class OnCategoryChanged(val category: CategoryEntity): ProductFormEvent

    data class OnImageChanged(val image: ProductTypeImage): ProductFormEvent

    data class OnQuantityTypeChanged(val quantityType: QuantityType?): ProductFormEvent

    data class OnQuantityChanged(val quantity: Int): ProductFormEvent
}