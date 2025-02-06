package com.felipemz.inventaryapp.ui.product

import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.*

class ProductFormViewModel : BaseViewModel<ProductFormState, ProductFormEvent>() {

    private val fakeChips = listOf(
        CategoryEntity(position = 1, name = "Alimentos", color = R.color.red_dark),
        CategoryEntity(position = 2, name = "Bebidas", color = R.color.blue),
        CategoryEntity(position = 3, name = "Cuidado Personal", color = R.color.pink),
        CategoryEntity(position = 4, name = "Electrónicos", color = R.color.teal),
        CategoryEntity(position = 5, name = "Hogar", color = R.color.orange),
        CategoryEntity(position = 6, name = "Limpieza", color = R.color.purple),
        CategoryEntity(position = 7, name = "Mascotas", color = R.color.green),
        CategoryEntity(position = 8, name = "Otros", color = R.color.lime)
    )

    private val  initialState = ProductFormState(
        categories = fakeChips,
        images = listOf(
            ProductTypeImage.LetterImage(String()),
            ProductTypeImage.EmojiImage(String()),
            ProductTypeImage.PhatImage(String())
        )
    )

    override fun initState() = initialState

    override fun intentHandler() {
        executeIntent { event ->
            when (event) {
                is Init -> {}
                is OnProductDeleted -> updateState { initialState }
                is OnCategoryChanged -> { updateState { it.copy(category = event.category) } }
                is OnNameChanged -> nameChanged(event.name)
                is OnDescriptionChanged -> { updateState { it.copy(description = event.description) } }
                is OnPriceChanged -> { updateState { it.copy(price = event.price) } }
                is OnImageChanged -> imageChanged(event.image)
                is OnQuantityTypeChanged -> quantityTypeChanged(event.quantityType)
                is OnQuantityChanged -> { updateState { it.copy(quantity = event.quantity) } }
                else -> Unit
            }
        }
    }

    private fun quantityTypeChanged(quantityType: QuantityType?) {
        updateState {
            it.copy(
                quantityType = quantityType,
                quantity = 0
            )
        }
    }

    private fun imageChanged(selection: ProductTypeImage) {
        updateState {
            it.copy(
                imageSelected = selection.ifNotEmpty(it.imageSelected.ifNotEmpty(it.images.first())),
                images = state.value.images.map { image ->
                    if (image::class == selection::class) selection
                    else image
                }
            )
        }
    }

    private fun nameChanged(name: String) {
        updateState {
            it.copy(
                name = name,
                images = state.value.images.map { image ->
                    when (image) {
                        is ProductTypeImage.LetterImage -> image.copy(name.take(2))
                        else -> image
                    }
                }
            )
        }
    }
}