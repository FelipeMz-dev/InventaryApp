package com.felipemz.inventaryapp.ui.movements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import com.felipemz.inventaryapp.domain.usecase.ObserveAllProductsUseCase
import com.felipemz.inventaryapp.ui.commons.AmountInvoiceItem
import com.felipemz.inventaryapp.ui.commons.InvoiceActions
import com.felipemz.inventaryapp.ui.commons.InvoiceItem
import com.felipemz.inventaryapp.ui.commons.ProductInvoiceItem
import com.felipemz.inventaryapp.ui.commons.getAmounts
import com.felipemz.inventaryapp.ui.commons.getProducts
import kotlinx.coroutines.Dispatchers
import kotlin.collections.any
import kotlin.collections.filterNot

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
                is MovementsEvent.OnSelectProduct -> selectProduct(it.item)
                is MovementsEvent.OnSelectProductFromBarcode -> selectProductFromBarcode(it.barcode)
                is MovementsEvent.OnChangeDiscount -> updateState { uiState ->
                    uiState.copy(discount = it.discount)
                }
                is MovementsEvent.OnExecuteAction -> action.postValue(it.action)
                is MovementsEvent.OnInvoiceAction -> handleInvoiceAction(it.action)
                else -> Unit
            }
        }
    }

    private fun handleInvoiceAction(action: InvoiceActions) = with(state.value) {

        fun addInvoiceItem(item: InvoiceItem) {
            if (selectedProducts.any { it.isEqualTo(item) }) {
                updateState { state ->
                    state.copy(
                        selectedProducts = state.selectedProducts.map {
                            if (it.isEqualTo(item)) it.copy(quantity = it.quantity + 1) else it
                        }
                    )
                }
            }
        }

        fun subtractInvoiceItem(item: InvoiceItem) {
            if (selectedProducts.any { it.isEqualTo(item) }) {
                updateState { state ->
                    state.copy(
                        selectedProducts = state.selectedProducts.mapNotNull {
                            if (it.isEqualTo(item)) {
                                val newValue = it.quantity - 1
                                it.copy(quantity = newValue).takeIf { newValue > 0 }
                            } else it
                        }
                    )
                }
            }
        }

        fun insertInvoiceItem(item: InvoiceItem) {
            if (selectedProducts.any { it.isEqualTo(item) }) addInvoiceItem(item)
            else updateState { it.copy(selectedProducts = it.selectedProducts.plus(item)) }
        }

        fun removeInvoiceItem(item: InvoiceItem) {
            selectedProducts.any { it.isEqualTo(item) }.ifTrue {
                updateState { it.copy(selectedProducts = it.selectedProducts.filterNot { it.isEqualTo(item) }) }
            }
        }

        when (action) {
            is InvoiceActions.OnInsertItem -> insertInvoiceItem(action.item)
            is InvoiceActions.OnRemoveItem -> removeInvoiceItem(action.item)
            is InvoiceActions.OnAddItem -> addInvoiceItem(action.item)
            is InvoiceActions.OnSubtractItem -> subtractInvoiceItem(action.item)
            else -> Unit
        }
    }

    private fun observeAllProducts() = execute(Dispatchers.IO) {
        observeAllProductsUseCase().collect { products ->
            updateState { it.copy(productList = products) }
        }
    }

    private fun selectProduct(product: InvoiceItem) {
        updateState { uiState ->
            val updatedList = when (product) {
                is ProductInvoiceItem -> updateSelectedProducts(uiState.selectedProducts, product)
                is AmountInvoiceItem -> {
                    updateAmountProducts(uiState.selectedProducts.getAmounts(), product)
                }
                else -> emptyList()
            }
            uiState.copy(selectedProducts = updatedList)
        }.invokeOnCompletion { calculateTotal() }
    }

    private fun updateSelectedProducts(
        products: List<InvoiceItem>,
        product: ProductInvoiceItem
    ): List<InvoiceItem> {
        return if (products.any { it is ProductInvoiceItem && it.product == product.product }) {
            products.mapNotNull {
                if (it is ProductInvoiceItem && it.product == product.product) {
                    it.copy(quantity = product.quantity).takeIf { product.quantity > 0 }
                } else it
            }
        } else {
            products + product
        }
    }

    private fun updateAmountProducts(
        amounts: List<AmountInvoiceItem>,
        amount: AmountInvoiceItem
    ): List<InvoiceItem> {
        return if (amount.quantity == 0) {
            state.value.selectedProducts.filterNot {
                it is AmountInvoiceItem && it.value == amount.value
            }
        } else {
            if (amount.value in amounts.map { it.value }) {
                state.value.selectedProducts.map {
                    if (it is AmountInvoiceItem && it.value == amount.value) amount else it
                }
            } else {
                state.value.selectedProducts + amount
            }
        }
    }

    private fun selectProductFromBarcode(barcode: String) = updateState { uiState ->
        uiState.productList.firstOrNull { it.barcode == barcode }?.let { product ->
            if (product in uiState.selectedProducts.getProducts().map { it.product }) {
                val updatedProduct = uiState.selectedProducts
                    .first { it is ProductInvoiceItem && it.product == product }
                    .let { it.copy(quantity = it.quantity + 1) }
                uiState.copy(
                    selectedProducts = uiState.selectedProducts.map {
                        if (it is ProductInvoiceItem && it.product == product) updatedProduct else it
                    }
                )
            } else {
                val newProduct = ProductInvoiceItem(product, 1)
                uiState.copy(selectedProducts = uiState.selectedProducts + newProduct)
            }
        } ?: run {
            uiState.copy(errorBarcode = barcode)
        }
    }

    private fun calculateTotal() {
        val subTotal = state.value.selectedProducts.sumOf {
            when (it) {
                is ProductInvoiceItem -> it.product.price
                is AmountInvoiceItem -> it.value
                else -> 0
            } * it.quantity
        }
        updateState { uiState ->
            uiState.copy(
                subTotal = subTotal,
                total = subTotal - uiState.discount
            )
        }
    }
}