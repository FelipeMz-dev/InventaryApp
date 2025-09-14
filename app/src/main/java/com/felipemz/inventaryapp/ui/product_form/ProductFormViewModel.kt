package com.felipemz.inventaryapp.ui.product_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.core.charts.CategoryUseChart
import com.felipemz.inventaryapp.core.charts.PackageUseChart
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.ifNotEmpty
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.extensions.orEmpty
import com.felipemz.inventaryapp.core.extensions.orFalse
import com.felipemz.inventaryapp.core.extensions.orTrue
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
import com.felipemz.inventaryapp.domain.usecase.ObserveQuantityProducts
import com.felipemz.inventaryapp.domain.usecase.VerifyBarcodeUseCase
import com.felipemz.inventaryapp.domain.usecase.VerifyPackagedProductUseCase
import com.felipemz.inventaryapp.ui.commons.actions.BillActions
import com.felipemz.inventaryapp.ui.commons.delegate.InvoiceItemsDelegate
import com.felipemz.inventaryapp.ui.commons.delegate.InvoiceItemsDelegateImpl
import com.felipemz.inventaryapp.ui.commons.delegate.ProductListFilterDelegate
import com.felipemz.inventaryapp.ui.commons.delegate.ProductListFilterDelegateImpl
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
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnPackageAction
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnPriceChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnProductDeleted
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnProductSaved
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnQuantityChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnQuantityTypeChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnSetCategoryFilter
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnSetNameFilter
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnSortCategories
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnTogglePackage
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnTryDeleteProduct
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.SetCategoryToChange
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.SetChangedSuccessfulCategory
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.SetChangedSuccessfulPackage
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.SetPackageToChange
import com.felipemz.inventaryapp.ui.product_form.components.alert_dialog.AlertDialogProductFormType
import kotlinx.coroutines.Dispatchers

class ProductFormViewModel(
    private val observeQuantityProducts: ObserveQuantityProducts,
    private val insertOrUpdateProductUseCase: InsertOrUpdateProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getProductsIdAndNameFromCategoryUseCase: GetProductsIdAndNameFromCategoryUseCase,
    private val verifyPackagedProductUseCase: VerifyPackagedProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val observeAllCategoriesUseCase: ObserveAllCategoriesUseCase,
    private val insertOrUpdateCategoryUseCase: InsertOrUpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryIfNotUseUseCase,
    private val verifyBarcodeUseCase: VerifyBarcodeUseCase,
) : BaseViewModel<ProductFormState, ProductFormEvent>() {

    private val action = MutableLiveData<ProductFormAction>()
    val actionLiveData: LiveData<ProductFormAction> = action

    private val invoiceDelegate: InvoiceItemsDelegate = InvoiceItemsDelegateImpl(
        getBillList = { state.value.packageList ?: emptyList() },
        updateBillList = ::updateBillListAndQuantity
    )

    private val productsFilteredDelegate: ProductListFilterDelegate = ProductListFilterDelegateImpl()

    val productList = productsFilteredDelegate.filteredProductList

    override fun initState() = ProductFormState()

    override fun intentHandler() {
        executeIntent { event ->
            when (event) {
                is Init -> init(event.productId, event.barcode, event.categoryId)
                is CloseAlertDialog -> closeAlertDialog()
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
                is OnDescriptionChanged -> descriptionChanged(event.description)
                is OnCostChanged -> costChanged(event.cost)
                is OnBarcodeChanged -> barcodeChanged(event.barcode)
                is OnQuantityTypeChanged -> quantityTypeChanged(event.quantityType)
                is OnQuantityChanged -> quantityChanged(event.quantity)
                is OnTogglePackage -> togglePackage(event.value)
                is SetCategoryToChange -> setCategoryToChange(event.categoryId)
                is SetPackageToChange -> setPackageToChange(event.packageId)
                is SetChangedSuccessfulCategory -> setChangedSuccessfulCategory(event.productId)
                is SetChangedSuccessfulPackage -> setChangedSuccessfulPackage(event.packageId)
                is OnPackageAction -> handlePackageAction(event.action)
                is OnSetNameFilter -> productsFilteredDelegate.setFilterName(event.name)
                is OnSetCategoryFilter -> productsFilteredDelegate.setFilterCategory(event.category)
                else -> Unit
            }
        }
    }

    private fun handlePackageAction(action: BillActions) {
        when (action) {
            is BillActions.OnInsertItem -> invoiceDelegate.insertInvoiceItem(action.item)
            is BillActions.OnRemoveItem -> invoiceDelegate.removeInvoiceItem(action.item)
            is BillActions.OnAddItem -> invoiceDelegate.addInvoiceItem(action.item)
            is BillActions.OnSubtractItem -> invoiceDelegate.subtractInvoiceItem(action.item)
            is BillActions.OnUpdateItem -> invoiceDelegate.updateInvoiceItem(action.item)
        }
    }

    private fun init(
        productId: Int?,
        barcode: String?,
        categoryId: Int?,
    ) {
        productId?.let { loadProduct(it) }
        barcode?.let { createFromBarcode(it) }
        observeAllCategories(categoryId)
        observeAllQuantityProducts()
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
                val packs = getPackageProducts(product)
                uiState.copy(
                    editProduct = product,
                    price = product.price,
                    category = product.category,
                    description = product.description,
                    cost = product.cost,
                    barcode = product.barcode,
                    quantityType = product.quantityModel?.type,
                    quantity = product.quantityModel?.quantity ?: 0,
                    packageList = packs
                )
            }
            imageChanged(product.image)
            nameChanged(product.name)
        }
    }

    private fun observeAllCategories(initialCategory: Int?) = execute(Dispatchers.IO) {
        var isFirst = true
        observeAllCategoriesUseCase().collect { categories ->
            updateState { it.copy(categories = categories) }
            if (isFirst) {
                isFirst = false
                categories.firstOrNull { it.id == initialCategory }?.let {
                    categoryChanged(it)
                }
            }
        }
    }

    private fun observeAllQuantityProducts() = execute(Dispatchers.IO) {
        observeQuantityProducts().collect { products ->
            val list = products.filterNot { it.id == state.value.editProduct?.id }
            productsFilteredDelegate.setProductList(list)
        }
    }

    private fun nameChanged(name: String) {
        updateState { uiState ->
            uiState.copy(
                name = name,
                images = state.value.images.map {
                    val isLetter = it is ProductTypeImage.LetterImage
                    if (isLetter) it.copy(name.take(2)) else it
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

    private fun descriptionChanged(description: String) {
        updateState { it.copy(description = description) }
    }

    private fun costChanged(cost: Int?) {
        updateState { it.copy(cost = cost) }
    }

    private fun imageChanged(selection: ProductTypeImage) {
        updateState {
            val defaultImage = it.imageSelected.ifNotEmptyOrDefault(it.images.first())
            it.copy(
                imageSelected = selection.ifNotEmptyOrDefault(defaultImage),
                images = state.value.images.map { image ->
                    if (image::class == selection::class) selection else image
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

    private fun quantityChanged(quantity: Int) {
        updateState { it.copy(quantity = quantity) }
    }

    private fun closeAlertDialog() {
        updateState { it.copy(alertDialog = null) }
    }

    private fun setCategoryToChange(categoryId: Int) {
        updateState { it.copy(categoryIdToChange = categoryId) }
    }

    private fun setPackageToChange(packageId: Int) {
        updateState { it.copy(packageIdToChange = packageId) }
    }

    private fun togglePackage(value: Boolean) {
        updateState { uiState ->
            if (value) {
                uiState.copy(
                    packageList = emptyList(),
                    quantityType = QuantityType.UNIT,
                )
            } else {
                uiState.copy(
                    packageList = null,
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
                    uiState.packageList?.minOf {
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
                && packageList?.isNotEmpty().orTrue()
        updateState { it.copy(enableToSave = isEnable) }
    }

    private fun saveProduct() = execute(Dispatchers.IO) {
        verifyQuantityChangeOnPackaged().ifNotEmpty {
            val dialog = AlertDialogProductFormType.UpdateQuantityProductPackaged(it)
            updateState { state -> state.copy(alertDialog = dialog) }
            return@execute
        }
        makeProduct()?.let { product ->
            insertOrUpdateProductUseCase(product)
            updateSavedProduct(product)
        }
    }

    private suspend fun verifyQuantityChangeOnPackaged(): List<PackageUseChart> = with(state.value) {
        editProduct?.let { product ->
            if (product.quantityModel?.isNull() != quantityType.isNull()) {
                verifyPackagedProductUseCase.invoke(product.id).map {
                    PackageUseChart(
                        usedId = it.id,
                        usesName = it.name,
                    )
                }
            } else emptyList()
        } ?: emptyList()
    }


    private fun updateSavedProduct(product: ProductModel) {
        with(state.value) {
            when {
                categoryIdToChange != null -> {
                    if (categoryIdToChange == product.category.id) return
                    action.postValue(ProductFormAction.ShowMessage("Categoría actualizada"))
                    action.postValue(ProductFormAction.OnCategoryChangeDone(product.id))
                }
                packageIdToChange != null -> {
                    if (packageIdToChange in product.packageProducts?.map { it.productId }.orEmpty()) return
                    action.postValue(ProductFormAction.ShowMessage("Paquete actualizado"))
                    action.postValue(ProductFormAction.OnPackageChangeDone(product.id))
                }
                else -> {
                    if (barcodeCreation) {
                        action.postValue(ProductFormAction.OnCreateFromBarcode(product.barcode.orEmpty()))
                    } else {
                        action.postValue(ProductFormAction.ShowMessage("Producto guardado"))
                        cleanData()
                    }
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
                packageProducts = packageList?.map { it.toProductPackageModel() }
            )
        }
    }

    private fun tryToDeleteProduct() = execute(Dispatchers.IO) {
        state.value.editProduct?.let { product ->
            val packagedProducts = verifyPackagedProductUseCase.invoke(product.id).map {
                PackageUseChart(
                    usedId = it.id,
                    usesName = it.name,
                )
            }
            val dialog = if (packagedProducts.isEmpty()) AlertDialogProductFormType.DeleteProduct(product)
            else AlertDialogProductFormType.DeleteProductPackaged(packagedProducts)
            updateState { it.copy(alertDialog = dialog) }
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

    private fun setChangedSuccessfulPackage(productId: Int) {
        updateState { uiState ->
            if (uiState.alertDialog is AlertDialogProductFormType.DeleteProductPackaged) {
                uiState.copy(
                    alertDialog = AlertDialogProductFormType.DeleteProductPackaged(
                        packagesUsesError = uiState.alertDialog.packagesUsesError.map {
                            if (it.usedId == productId) it.copy(isChanged = true) else it
                        }
                    ),
                )
            } else uiState
        }
    }

    private fun getPackageProducts(product: ProductModel): List<BillItemChart>? {
        if (product.packageProducts.isNullOrEmpty()) return null //TODO: move to use case
        return product.packageProducts.mapNotNull { item ->
            productList.value.firstOrNull { it.id == item.productId }?.let {
                BillItemChart(
                    product = it,
                    quantity = item.quantity
                )
            }
        }
    }

    private fun BillItemChart.toProductPackageModel() = ProductPackageModel(
        productId = product?.id ?: 0,
        quantity = quantity,
    )

    private fun updateBillListAndQuantity(newList: List<BillItemChart>) {
        val min = newList.minOfOrNull { (it.product?.quantityModel?.quantity ?: 0) / it.quantity }
        updateState {
            it.copy(
                packageList = newList,
                quantity = min?.coerceAtLeast(0) ?: 0
            )
        }.invokeOnCompletion {
            validateEnableToSave()
        }
    }

    private fun cleanData() {
        updateState { ProductFormState() }
    }
}