package com.felipemz.inventaryapp.ui.product_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.ifNotEmpty
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.orDefault
import com.felipemz.inventaryapp.core.extensions.orFalse
import com.felipemz.inventaryapp.core.extensions.orTrue
import com.felipemz.inventaryapp.core.extensions.tryOrDefault
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.model.ProductQuantityModel
import com.felipemz.inventaryapp.domain.model.ProductSelectionChart
import com.felipemz.inventaryapp.domain.model.ProductTypeImage
import com.felipemz.inventaryapp.domain.model.toProductPackageModel
import com.felipemz.inventaryapp.domain.usecase.DeleteCategoryIfNotUseUseCase
import com.felipemz.inventaryapp.domain.usecase.DeleteProductUseCase
import com.felipemz.inventaryapp.domain.usecase.GetProductByIdUseCase
import com.felipemz.inventaryapp.domain.usecase.GetProductsIdAndNameFromCategoryUseCase
import com.felipemz.inventaryapp.domain.usecase.InsertOrUpdateCategoryUseCase
import com.felipemz.inventaryapp.domain.usecase.InsertOrUpdateProductUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveAllCategoriesUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveProductsNotPackaged
import com.felipemz.inventaryapp.domain.usecase.VerifyBarcodeUseCase
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.*
import com.felipemz.inventaryapp.ui.product_form.components.alert_dialog.AlertDialogProductFormType
import kotlinx.coroutines.Dispatchers

class ProductFormViewModel(
    private val observeProductsNotPackaged: ObserveProductsNotPackaged,
    private val insertOrUpdateProductUseCase: InsertOrUpdateProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getProductsIdAndNameFromCategoryUseCase: GetProductsIdAndNameFromCategoryUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val observeAllCategoriesUseCase: ObserveAllCategoriesUseCase,
    private val insertOrUpdateCategoryUseCase: InsertOrUpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryIfNotUseUseCase,
    private val verifyBarcodeUseCase: VerifyBarcodeUseCase,
) : BaseViewModel<ProductFormState, ProductFormEvent>() {

    private val action = MutableLiveData<ProductFormAction>()
    val actionLiveData: LiveData<ProductFormAction> = action

    override fun initState() = ProductFormState()

    override fun intentHandler() {
        executeIntent { event ->
            when (event) {
                is Init -> init(event.productId)
                is CloseAlertDialog -> updateState { it.copy(alertDialog = null) }
                is OnTryDeleteProduct -> tryToDeleteProduct()
                is OnProductDeleted -> deleteProduct()
                is OnInsertOrUpdateCategory -> insertOrUpdateCategory(event.category)
                is OnSortCategories -> sortCategories(event.from, event.to)
                is OnDeleteCategory -> tryDeleteCategory(event.categoryId)
                is OnProductSaved -> saveProduct()
                is OnNameChanged -> nameChanged(event.name)
                is OnPriceChanged -> priceChanged(event.price)
                is OnCategoryChanged -> categoryChanged(event.category)
                is OnImageChanged -> imageChanged(event.image)
                is OnDescriptionChanged -> updateState { it.copy(description = event.description) }
                is OnCostChanged -> updateState { it.copy(cost = event.cost) }
                is OnBarcodeChanged -> barcodeChanged(event.barcode)
                is OnQuantityTypeChanged -> quantityTypeChanged(event.quantityType)
                is OnQuantityChanged -> updateState { it.copy(quantity = event.quantity) }
                is OnPackageProductSelect -> packageProductSelect(event.product)
                is SetCategoryToChange -> updateState { it.copy(categoryIdToChange = event.categoryId) }
                is SetChangedSuccessfulCategory -> setChangedSuccessfulCategory(event.productId)
                else -> Unit
            }
        }
    }

    private fun barcodeChanged(barcode: String?) = execute(Dispatchers.IO) {
        val showAlertBarcode = barcode?.let { verifyBarcodeUseCase(it) }.orFalse()
        updateState {
            it.copy(
                barcode = barcode,
                alertBarcode = showAlertBarcode,
            )
        }.invokeOnCompletion {
            validateEnableToSave()
        }
    }

    private fun init(productId: Int?) {
        productId?.let { loadProduct(it) }
        observeAllCategories()
        observeAllProductsNotPackaged()
    }

    private fun loadProduct(productId: Int) = execute(Dispatchers.IO) {
        getProductByIdUseCase(productId)?.let { product ->
            updateState { uiState ->
                uiState.copy(
                    editProduct = product,
                    price = product.price,
                    category = product.category,
                    description = product.description,
                    cost = product.cost,
                    barcode = product.barCode,
                    quantityType = product.quantityModel?.type,
                    quantity = product.quantityModel?.quantity ?: 0,
                    packageProducts = getPackageProducts(product)
                )
            }
            imageChanged(product.image)
            nameChanged(product.name)
        }
    }

    private fun observeAllCategories() = execute(Dispatchers.IO) {
        observeAllCategoriesUseCase().collect { categories ->
            updateState { it.copy(categories = categories) }
        }
    }

    private fun observeAllProductsNotPackaged() = execute(Dispatchers.IO) {
        observeProductsNotPackaged().collect { products ->
            updateState { it.copy(productList = products) }
        }
    }

    private fun getPackageProducts(product: ProductModel): List<ProductSelectionChart>? {
        return product.packageProducts?.map { productSelection ->
            ProductSelectionChart(
                product = state.value.productList.firstOrNull { it.id == productSelection.productId },
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
            updateImageLetter()
            validateEnableToSave()
        }
    }

    private fun updateImageLetter() = with(state.value) {
        if (imageSelected is ProductTypeImage.LetterImage) updateState {
            it.copy(imageSelected = images.first())
        }
    }

    private fun priceChanged(price: Int) {
        updateState { it.copy(price = price) }.invokeOnCompletion {
            validateEnableToSave()
        }
    }

    private fun categoryChanged(category: CategoryModel) {
        updateState { it.copy(category = category) }.invokeOnCompletion {
            validateEnableToSave()
        }
    }

    private fun imageChanged(selection: ProductTypeImage) {
        updateState {
            it.copy(
                imageSelected = selection.ifNotEmptyOrDefault(it.imageSelected.ifNotEmptyOrDefault(it.images.first())),
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

    private fun packageProductSelect(product: ProductSelectionChart?) {
        updateState { uiState ->
            product?.let { compositionProduct ->
                uiState.copy(
                    packageProducts = uiState.packageProducts?.let { products ->
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
                packageProducts = null,
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
                    uiState.packageProducts?.minOf {
                        (it.product?.quantityModel?.quantity ?: 0).floorDiv(it.quantity)
                    } ?: 0
                }
            )
        }
    }

    private fun validateEnableToSave() = with(state.value) {
        val isEnable = name.isNotEmpty()
                && price > 0
                && category.isNotNull()
                && !alertBarcode
                && packageProducts
            ?.isNotEmpty()
            .orDefault(true)
        updateState { it.copy(enableToSave = isEnable) }
    }

    private fun saveProduct() = execute(Dispatchers.IO) {
        val product = makeProduct()
        product?.let { product ->
            insertOrUpdateProductUseCase(product)
            state.value.categoryIdToChange?.let { categoryId ->
                if (categoryId == product.category.id) return@let
                action.postValue(ProductFormAction.ShowMessage("Categoría actualizada"))
                action.postValue(ProductFormAction.OnCategoryChangeDone(product.id))
            } ?: run {
                action.postValue(ProductFormAction.ShowMessage("Producto guardado"))
                cleanData()
            }
        }
    }

    private fun makeProduct() = with(state.value) {
        category?.let { category ->
            ProductModel(
                id = editProduct?.id ?: 0,
                name = name,
                price = price,
                category = category,
                image = imageSelected,
                description = description?.trim()?.takeIf { it.isNotEmpty() },
                barCode = barcode?.trim()?.takeIf { it.isNotEmpty() },
                cost = cost.takeIf { it != 0 },
                quantityModel = quantityType?.let { ProductQuantityModel(it, quantity) },
                packageProducts = packageProducts?.map { it.toProductPackageModel() }
            )
        }
    }

    private fun tryToDeleteProduct() {
        state.value.editProduct?.let { product ->
            updateState {
                it.copy(
                    alertDialog = AlertDialogProductFormType.DeleteProduct(product)
                )
            }
        }
    }

    private fun deleteProduct() = execute(Dispatchers.IO) {
        state.value.editProduct?.id?.let {
            deleteProductUseCase(it)
            action.postValue(ProductFormAction.ShowMessage("Producto eliminado"))
            cleanData()
        }
    }

    private fun insertOrUpdateCategory(category: CategoryModel) = execute(Dispatchers.IO) {
        val newCategory = insertOrUpdateCategoryUseCase(category)
        newCategory?.let(::categoryChanged)
    }

    private fun sortCategories(
        from: CategoryModel,
        to: CategoryModel
    ) = execute(Dispatchers.IO) {
        insertOrUpdateCategoryUseCase(from)
        insertOrUpdateCategoryUseCase(to)
    }

    private fun tryDeleteCategory(categoryId: Int) = execute(Dispatchers.IO) {
        deleteCategoryUseCase(categoryId).let { isDeleted ->
            isDeleted.ifTrue {
                action.postValue(ProductFormAction.ShowMessage("Categoría eliminada"))
                if (categoryId == state.value.category?.id) {
                    updateState { it.copy(category = null) }
                }
                return@execute
            }
            validateCategoryUses(categoryId)
        }
    }

    private fun validateCategoryUses(categoryId: Int) = execute(Dispatchers.IO) {
        getProductsIdAndNameFromCategoryUseCase(categoryId).let { productIds ->
            updateState {
                it.copy(
                    alertDialog = AlertDialogProductFormType.DeleteCategory(
                        categoryId = categoryId,
                        categoryUsesError = productIds.map { idAndName ->
                            CategoryUseChart(
                                usedId = idAndName.first,
                                usesName = idAndName.second,
                                isChanged = false
                            )
                        }
                    )
                )
            }
        }
    }

    private fun setChangedSuccessfulCategory(productId: Int) {
        updateState { uiState ->
            if (uiState.alertDialog is AlertDialogProductFormType.DeleteCategory) {
                uiState.copy(
                    alertDialog = AlertDialogProductFormType.DeleteCategory(
                        categoryId = uiState.alertDialog.categoryId,
                        categoryUsesError = uiState.alertDialog.categoryUsesError.map {
                            if (it.usedId == productId) it.copy(isChanged = true) else it
                        }
                    ),
                )
            } else uiState
        }
    }

    private fun cleanData() {
        updateState { ProductFormState() }
    }
}
