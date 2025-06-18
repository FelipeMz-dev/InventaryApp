package com.felipemz.inventaryapp.ui.movements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.domain.model.ProductSelectionChart
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import com.felipemz.inventaryapp.domain.usecase.ObserveAllProductsUseCase
import com.felipemz.inventaryapp.ui.product_form.ProductFormAction
import kotlinx.coroutines.Dispatchers

class MovementsViewModel(
    private val observeAllProductsUseCase: ObserveAllProductsUseCase,
) : BaseViewModel<MovementsState, MovementsEvent>() {

    private val action = MutableLiveData<MovementsAction>()
    val actionLiveData: LiveData<MovementsAction> = action

    override fun initState() = MovementsState(
        movementDate = FormatDateUtil.getOfFilterChip(ReportsFilterDate.TODAY)
    )

    override fun intentHandler() {
        executeIntent {
            when (it) {
                is MovementsEvent.Init -> {
                    observeAllProducts()
                }
                is MovementsEvent.OnClearBarcodeError -> updateState { uiState ->
                    uiState.copy(errorBarcode = null)
                }
                is MovementsEvent.OnSelectProduct -> selectProduct(it.product)
                is MovementsEvent.OnSelectProductFromBarcode -> selectProductFromBarcode(it.barcode)
                is MovementsEvent.OnChangeDiscount -> updateState { uiState ->
                    uiState.copy(discount = it.discount)
                }
                is MovementsEvent.IncrementCalculatorId -> updateState { uiState ->
                    uiState.copy(calculatorId = uiState.calculatorId + 1)
                }
                is MovementsEvent.OnExecuteAction -> action.postValue(it.action)
                else -> Unit
            }
        }
    }

    private fun observeAllProducts() = execute(Dispatchers.IO) {
        observeAllProductsUseCase().collect { products ->
            updateState { it.copy(productList = products) }
        }
    }

    private fun selectProduct(product: ProductSelectionChart) {
        updateState { uiState ->
            uiState.copy(
                selectedProducts = uiState.selectedProducts.let { products ->
                    if (products.any { it.product == product.product }) {
                        products.mapNotNull {
                            if (it.product != product.product) it
                            else it.copy(quantity = product.quantity).takeIf {
                                product.quantity > 0
                            }
                        }
                    } else products + product
                }.takeIf { product.product.isNotNull() } ?: run {
                    if (product.quantity == 0){
                        uiState.selectedProducts.filterNot { it.price == product.price }
                    } else {
                        val listNotProduct = uiState.selectedProducts.filter { it.product.isNull() }
                        if (product.price in listNotProduct.map { it.price }) {
                            uiState.selectedProducts.map {
                                if (it.product.isNull() && product.price == it.price) product else it
                            }
                        }
                        else uiState.selectedProducts + product
                    }
                }
            )
        }.invokeOnCompletion { calculateTotal() }
    }

    private fun selectProductFromBarcode(barcode: String) = updateState { uiState ->
        uiState.productList.firstOrNull { it.barcode == barcode }?.let { product ->
            if (product in uiState.selectedProducts.map { it.product }) {
                val updatedProduct = uiState.selectedProducts
                    .first { it.product == product }
                    .let { it.copy(quantity = it.quantity + 1) }
                uiState.copy(
                    selectedProducts = uiState.selectedProducts.map {
                        if (it.product == product) updatedProduct else it
                    }
                )
            } else {
                val newProduct = ProductSelectionChart(product, 1)
                uiState.copy(selectedProducts = uiState.selectedProducts + newProduct)
            }
        } ?: run {
            uiState.copy(errorBarcode = barcode)
        }
    }

    private fun calculateTotal() {
        val subTotal = state.value.selectedProducts.sumOf { (it.product?.price ?: it.price) * it.quantity }
        updateState { uiState ->
            uiState.copy(
                subTotal = subTotal,
                total = subTotal - uiState.discount
            )
        }
    }
}