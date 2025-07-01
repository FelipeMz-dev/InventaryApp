package com.felipemz.inventaryapp.ui.product_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.orDefault
import com.felipemz.inventaryapp.core.extensions.orFalse
import com.felipemz.inventaryapp.core.extensions.tryOrDefault
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.model.ProductPackageModel
import com.felipemz.inventaryapp.domain.model.ProductQuantityModel
import com.felipemz.inventaryapp.domain.model.ProductTypeImage
import com.felipemz.inventaryapp.domain.usecase.DeleteCategoryIfNotUseUseCase
import com.felipemz.inventaryapp.domain.usecase.DeleteProductUseCase
import com.felipemz.inventaryapp.domain.usecase.GetProductByIdUseCase
import com.felipemz.inventaryapp.domain.usecase.GetProductsIdAndNameFromCategoryUseCase
import com.felipemz.inventaryapp.domain.usecase.InsertOrUpdateCategoryUseCase
import com.felipemz.inventaryapp.domain.usecase.InsertOrUpdateProductUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveAllCategoriesUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveProductsNotPackaged
import com.felipemz.inventaryapp.domain.usecase.VerifyBarcodeUseCase
import com.felipemz.inventaryapp.ui.commons.InvoiceActions
import com.felipemz.inventaryapp.ui.commons.InvoiceItem
import com.felipemz.inventaryapp.ui.commons.ProductInvoiceItem
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.CloseAlertDialog
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.Init
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnBarcodeChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnCategoryChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnCostChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnDeleteCategory
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnDescriptionChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnImageChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnInsertOrUpdateCategory
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnNameChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnPackageProductSelect
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnPriceChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnProductDeleted
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnProductSaved
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnQuantityChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnQuantityTypeChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnSortCategories
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnTryDeleteProduct
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.SetCategoryToChange
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.SetChangedSuccessfulCategory
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
                is Init -> init(event.productId, event.barcode)
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
                is ProductFormEvent.OnTogglePackage -> togglePackage(event.value)
                is SetCategoryToChange -> updateState { it.copy(categoryIdToChange = event.categoryId) }
                is SetChangedSuccessfulCategory -> setChangedSuccessfulCategory(event.productId)
                is ProductFormEvent.OnPackageAction -> handlePackageAction(event.action)
                else -> Unit
            }
        }
    }

    private fun handlePackageAction(action: InvoiceActions) = with(state.value) {

        fun add(item: InvoiceItem) {
            updateState { state ->
                state.copy(
                    packageProducts = state.packageProducts?.map {
                        if (item.isEqualTo(it)) it.copy(quantity = it.quantity + 1) else it
                    }
                )
            }
        }

        fun subtract(item: ProductInvoiceItem) {
            updateState { state ->
                state.copy(
                    packageProducts = state.packageProducts?.mapNotNull {
                        if (item.isEqualTo(it)) {
                            val newValue = it.quantity - 1
                            it.copy(quantity = newValue).takeIf { newValue > 0 }
                        } else it
                    }
                )
            }
        }

        fun itemAdd(item: ProductInvoiceItem) {
            packageProducts?.any { it.isEqualTo(item) }?.ifTrue { add(item) }
        }

        fun itemSubtract(item: ProductInvoiceItem) {
            packageProducts?.any { it.isEqualTo(item) }?.ifTrue { subtract(item) }
        }

        fun itemInsert(item: ProductInvoiceItem) {
            if (packageProducts?.any { it.isEqualTo(item) } == true) itemAdd(item)
            else updateState { it.copy(packageProducts = it.packageProducts?.plus(item)) }
        }

        fun itemRemove(item: ProductInvoiceItem) {
            packageProducts?.any { it.isEqualTo(item) }?.ifTrue {
                updateState { it.copy(packageProducts = it.packageProducts?.filterNot { it.isEqualTo(item) }) }
            }
        }


        when (action) {
            is InvoiceActions.OnInsertItem -> (action.item as? ProductInvoiceItem)?.let { itemInsert(it) }
            is InvoiceActions.OnRemoveItem -> (action.item as? ProductInvoiceItem)?.let { itemRemove(it) }
            is InvoiceActions.OnAddItem -> (action.item as? ProductInvoiceItem)?.let { itemAdd(it) }
            is InvoiceActions.OnSubtractItem -> (action.item as? ProductInvoiceItem)?.let { itemSubtract(it) }
            else -> Unit
        }
    }

    private fun init(
        productId: Int?,
        barcode: String?
    ) {
        productId?.let { loadProduct(it) }
        barcode?.let { createFromBarcode(it) }
        observeAllCategories()
        observeAllProductsNotPackaged()
    }

    private fun createFromBarcode(barcode: String) {
        updateState {
            it.copy(
                barcode = barcode,
                barcodeCreation = true
            )
        }
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
                    barcode = product.barcode,
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

    private fun getPackageProducts(product: ProductModel): List<ProductInvoiceItem>? {
        return product.packageProducts?.mapNotNull { productSelection ->
            state.value.productList.firstOrNull { it.id == productSelection.productId }?.let {
                ProductInvoiceItem(
                    product = it,
                    quantity = productSelection.quantity
                )
            }
        }?.takeIf { it.isEmpty() }
    }

    private fun ProductInvoiceItem.toProductPackageModel() = ProductPackageModel(
        productId = product.id,
        quantity = quantity,
    )

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

    private fun quantityTypeChanged(quantityType: QuantityType?) {
        updateState {
            it.copy(
                quantityType = quantityType,
                quantity = 0
            )
        }
    }

    private fun packageProductSelect(product: ProductInvoiceItem) {
        updateState { uiState ->
            uiState.copy(
                packageProducts = uiState.packageProducts?.let { products ->
                    if (products.any { it.product == product.product }) {
                        products.mapNotNull {
                            if (it.product != product.product) it
                            else product.takeIf { it.quantity > 0 }
                        }
                    } else products + product
                },
                quantityType = QuantityType.UNIT,
            )
        }.invokeOnCompletion {
            updateQuantityComposition()
            validateEnableToSave()
        }
    }

    private fun togglePackage(value: Boolean) {
        updateState { uiState ->
            if (value) {
                uiState.copy(
                    packageProducts = emptyList(),
                    quantityType = QuantityType.UNIT,
                )
            } else {
                uiState.copy(
                    packageProducts = null,
                    quantityType = null,
                )
            }
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
                        (it.product.quantityModel?.quantity ?: 0).floorDiv(it.quantity)
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
                if (state.value.barcodeCreation) {
                    action.postValue(ProductFormAction.OnCreateFromBarcode(product.barcode.orEmpty()))
                } else {
                    action.postValue(ProductFormAction.ShowMessage("Producto guardado"))
                    cleanData()
                }
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
                barcode = barcode?.trim()?.takeIf { it.isNotEmpty() },
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
