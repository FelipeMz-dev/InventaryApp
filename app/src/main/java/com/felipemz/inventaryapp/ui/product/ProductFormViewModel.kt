package com.felipemz.inventaryapp.ui.product

import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.entitys.PackageProductType
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.toProductPackEntity
import com.felipemz.inventaryapp.core.entitys.toProductSelectedEntity
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.ui.commons.fakeChips
import com.felipemz.inventaryapp.ui.commons.fakeProducts
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.*

class ProductFormViewModel : BaseViewModel<ProductFormState, ProductFormEvent>() {


    private val  initialState = ProductFormState(
        categories = fakeChips,
        images = listOf(
            ProductTypeImage.LetterImage(String()),
            ProductTypeImage.EmojiImage(String()),
            ProductTypeImage.PhatImage(String())
        ),
        productList = fakeProducts
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
                is OnPackageTypeChanged -> packageTypeChanged(event.packageType)
                is OnAddProductToPack -> addProductToPack(event.productEntity)
                else -> Unit
            }
        }
    }

    private fun addProductToPack(productEntity: ProductEntity) {
        updateState {
            it.copy(
                packageType = when (it.packageType) {
                    is PackageProductType.Pack -> {
                        val products = it.packageType.products.toMutableList()
                        products.add(productEntity.toProductPackEntity())
                        PackageProductType.Pack(products)
                    }
                    is PackageProductType.Package -> {
                        val product = productEntity.toProductPackEntity()
                        PackageProductType.Package(product)
                    }
                    else -> null
                }
            )
        }
    }

    private fun quantityTypeChanged(quantityType: QuantityType?) {
        updateState {
            it.copy(
                quantityType = quantityType,
                quantity = 0,
                packageType = null,
            )
        }
    }

    private fun packageTypeChanged(packageType: PackageProductType?) {
        updateState {
            it.copy(
                packageType = packageType,
                quantityType = null,
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