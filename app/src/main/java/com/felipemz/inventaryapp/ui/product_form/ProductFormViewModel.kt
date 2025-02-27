package com.felipemz.inventaryapp.ui.product_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.ProductQuantityEntity
import com.felipemz.inventaryapp.core.entitys.toProductSelectionEntity
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.tryOrDefault
import com.felipemz.inventaryapp.ui.commons.fakeChips
import com.felipemz.inventaryapp.ui.commons.fakeProducts
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductQuantityChart
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.*

class ProductFormViewModel : BaseViewModel<ProductFormState, ProductFormEvent>() {

    private val messenger = MutableLiveData<String?>()
    val messengerLiveData: LiveData<String?> = messenger

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
                is Init -> init(event.productId)
                is OnProductDeleted -> deleteProduct()
                is OnProductSaved -> saveProduct()
                is OnNameChanged -> nameChanged(event.name)
                is OnPriceChanged -> priceChanged(event.price)
                is OnCategoryChanged -> categoryChanged(event.category)
                is OnImageChanged -> imageChanged(event.image)
                is OnDescriptionChanged -> updateState { it.copy(description = event.description) }
                is OnCostChanged -> updateState { it.copy(cost = event.cost) }
                is OnQuantityTypeChanged -> quantityTypeChanged(event.quantityType)
                is OnQuantityChanged -> updateState { it.copy(quantity = event.quantity) }
                is OnPackageProductSelect -> packageProductSelected(event.product)
                is OnCompositionProductSelect -> compositionProductSelect(event.product)
                is OnSubProductSelect -> subProductSelect(event.product)
                else -> Unit
            }
        }
    }

    private fun init(productId: Int?) {
        val product = state.value.productList.firstOrNull { it.id == productId }
        product?.let {
            updateState { uiState ->
                uiState.copy(
                    originalProduct = product,
                    price = product.price,
                    category = product.category,
                    description = product.description,
                    cost = product.cost,
                    quantityType = product.quantityChart?.type,
                    quantity = product.quantityChart?.quantity ?: 0,
                    packageProduct = getPackageProduct(product),
                    compositionProducts = getCompositionProducts(product)
                )
            }
            imageChanged(product.image)
            nameChanged(product.name)
        }
    }

    private fun getCompositionProducts(product: ProductEntity): List<ProductQuantityEntity>? {
        return product.compositionProducts?.map { productSelection ->
            ProductQuantityEntity(
                product = state.value.productList.firstOrNull { it.id == productSelection.id },
                quantity = productSelection.quantity
            )
        }
    }
    private fun getPackageProduct(product: ProductEntity): ProductQuantityEntity? {
        return product.packageProduct?.let { productSelection ->
            ProductQuantityEntity(
                product = state.value.productList.firstOrNull { it.id == productSelection.id },
                quantity = productSelection.quantity
            )
        }
    }

    private fun nameChanged(name: String) {
        updateState { uiState ->
            uiState.copy(
                name = name,
                images = state.value.images.map {
                    if (it is ProductTypeImage.LetterImage) it.copy(name.take(2)) else it
                }
            )
        }.invokeOnCompletion {
            validateEnableToSave()
        }
    }

    private fun priceChanged(price: Int) {
        updateState { it.copy(price = price) }.invokeOnCompletion {
            validateEnableToSave()
        }
    }

    private fun categoryChanged(category: CategoryEntity) {
        updateState { it.copy(category = category) }.invokeOnCompletion {
            validateEnableToSave()
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

    private fun quantityTypeChanged(quantityType: QuantityType?) {
        updateState {
            it.copy(
                quantityType = quantityType,
                quantity = 0
            )
        }
    }

    private fun subProductSelect(product: ProductQuantityEntity?) {
        state.value.packageProduct?.product?.run {
            packageProductSelected(product)
        } ?: compositionProductSelect(product)
    }

    private fun packageProductSelected(product: ProductQuantityEntity?) {
        updateState { uiState ->
            product?.let { packageProduct ->
                uiState.copy(
                    packageProduct = packageProduct.copy(
                        product = packageProduct.product.takeIf { packageProduct.quantity > 0 },
                        quantity = packageProduct.quantity
                    ),
                    quantityType = QuantityType.UNIT,
                    quantity = tryOrDefault(0) {
                        (packageProduct.product?.quantityChart?.quantity ?: 0).floorDiv(packageProduct.quantity)
                    }
                )
            } ?: uiState.copy(
                packageProduct = null,
                quantityType = null,
                quantity = 0
            )
        }.invokeOnCompletion {
            validateEnableToSave()
        }
    }

    private fun compositionProductSelect(product: ProductQuantityEntity?) {
        updateState { uiState ->
            product?.let { compositionProduct ->
                uiState.copy(
                    compositionProducts = uiState.compositionProducts?.let { products ->
                        if (products.any { it.product == compositionProduct.product }) {
                            products.mapNotNull {
                                if (it.product != product.product) it
                                else it.copy(quantity = compositionProduct.quantity).takeIf {
                                    compositionProduct.quantity > 0
                                }
                            }
                        } else products + compositionProduct
                    } ?: emptyList(),
                    quantityType = QuantityType.UNIT,
                )
            } ?: uiState.copy(
                compositionProducts = null,
                quantityType = null
            )
        }.invokeOnCompletion {
            updateQuantityComposition()
            validateEnableToSave()
        }
    }

    private fun updateQuantityComposition() {
        updateState { uiState ->
            uiState.copy(
                quantity = tryOrDefault(0) {
                    uiState.compositionProducts?.minOf {
                        (it.product?.quantityChart?.quantity ?: 0).floorDiv(it.quantity)
                    } ?: 0
                }
            )
        }
    }

    private fun validateEnableToSave() = with(state.value) {
        val isEnable = name.isNotEmpty()
                && price > 0
                && category.isNotNull()
                && packageProduct?.let { it.product.isNotNull() } ?: true
                && compositionProducts?.isNotEmpty() ?: true
        updateState { it.copy(enableToSave = isEnable) }
    }

    private fun saveProduct() {
        val product = with(state.value) {
            ProductEntity(
                id = originalProduct?.id ?: 0,
                name = name,
                price = price,
                category = category ?: CategoryEntity(),
                image = imageSelected,
                description = description,
                cost = cost,
                quantityChart = quantityType?.let {
                    ProductQuantityChart(
                        type = it,
                        quantity = quantity
                    )
                },
                packageProduct = packageProduct?.toProductSelectionEntity(),
                compositionProducts = compositionProducts?.map { it.toProductSelectionEntity() }
            )
        }
        messenger.postValue("Producto guardado")
        updateState { initialState }
    }

    private fun deleteProduct() {
        messenger.postValue("Producto eliminado")
        updateState { initialState }
    }
}
