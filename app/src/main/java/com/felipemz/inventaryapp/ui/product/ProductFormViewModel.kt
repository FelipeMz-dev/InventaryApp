package com.felipemz.inventaryapp.ui.product

import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.entitys.PackageProductModel
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.ProductPackEntity
import com.felipemz.inventaryapp.core.entitys.ProductSelectionEntity
import com.felipemz.inventaryapp.core.entitys.toProductPackEntity
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.ui.commons.fakeChips
import com.felipemz.inventaryapp.ui.commons.fakeProducts
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.*

class ProductFormViewModel : BaseViewModel<ProductFormState, ProductFormEvent>() {


    private val initialState = ProductFormState(
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
                is OnCategoryChanged -> {
                    updateState { it.copy(category = event.category) }
                }
                is OnNameChanged -> nameChanged(event.name)
                is OnDescriptionChanged -> {
                    updateState { it.copy(description = event.description) }
                }
                is OnPriceChanged -> {
                    updateState { it.copy(price = event.price) }
                }
                is OnImageChanged -> imageChanged(event.image)
                is OnQuantityTypeChanged -> quantityTypeChanged(event.quantityType)
                is OnQuantityChanged -> {
                    updateState { it.copy(quantity = event.quantity) }
                }
                is OnPackageTypeChanged -> packageTypeChanged(event.packageType)
                is OnAddProductToPack -> addProductToPack(event.product)
                is OnDeleteProductFromPack -> deleteProductFromPack(event.product)
                else -> Unit
            }
        }
    }

    private fun deleteProductFromPack(product: ProductPackEntity) = with(state.value) {
        val packModel = when (packageType) {
            is PackageProductModel.Pack -> {
                val products = packageType.products.filterNot { it == product }
                PackageProductModel.Pack(products)
            }
            is PackageProductModel.Package -> PackageProductModel.Package(null)
            else -> null
        }
        updateState { it.copy(packageType = packModel) }
    }

    private fun addProductToPack(product: ProductSelectionEntity) = with(state.value) {
        productList.find { it.id == product.id }?.let { current ->
            val packModel = when (packageType) {
                is PackageProductModel.Package -> PackageProductModel.Package(current.toProductPackEntity(1))
                is PackageProductModel.Pack -> PackageProductModel.Pack(
                    products = if (packageType.products.any { it.id == current.id }) {
                        packageType.products.mapNotNull {
                            if (it.id == product.id) current.toProductPackEntity(product.quantity).takeIf {
                                product.quantity > 0
                            } else it
                        }
                    } else packageType.products + current.toProductPackEntity(product.quantity)
                )
                else -> null
            }
            updateState { it.copy(packageType = packModel) }
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

    private fun packageTypeChanged(packageType: PackageProductModel?) {
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