package com.felipemz.inventaryapp.ui.movements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.ui.commons.actions.BillActions
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import com.felipemz.inventaryapp.domain.usecase.ObserveAllProductsUseCase
import com.felipemz.inventaryapp.ui.commons.delegate.InvoiceItemsDelegate
import com.felipemz.inventaryapp.ui.commons.delegate.InvoiceItemsDelegateImpl
import kotlinx.coroutines.Dispatchers

class MovementsViewModel(
    private val observeAllProductsUseCase: ObserveAllProductsUseCase,
) : BaseViewModel<MovementsState, MovementsEvent>() {

    private val action = MutableLiveData<MovementsAction>()
    val actionLiveData: LiveData<MovementsAction> = action

    private val invoiceDelegate: InvoiceItemsDelegate = InvoiceItemsDelegateImpl(
        getBillList = { state.value.billList },
        updateBillList = { newList -> updateState { it.copy(billList = newList) } }
    )

    override fun initState() = MovementsState(
        movementDate = FormatDateUtil.getOfFilterChip(ReportsFilterDate.TODAY)
    )

    override fun intentHandler() {
        executeIntent {
            when (it) {
                is MovementsEvent.Init -> observeAllProducts()
                is MovementsEvent.OnClearBarcodeError -> updateState { uiState ->
                    uiState.copy(errorBarcode = null)
                }
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

    private fun handleInvoiceAction(action: BillActions) = with(state.value) {
        when (action) {
            is BillActions.OnInsertItem -> invoiceDelegate.insertInvoiceItem(action.item)
            is BillActions.OnRemoveItem -> invoiceDelegate.removeInvoiceItem(action.item)
            is BillActions.OnAddItem -> invoiceDelegate.addInvoiceItem(action.item)
            is BillActions.OnSubtractItem -> invoiceDelegate.subtractInvoiceItem(action.item)
            is BillActions.OnUpdateItem -> invoiceDelegate.updateInvoiceItem(action.item)
        }
    }

    private fun observeAllProducts() = execute(Dispatchers.IO) {
        observeAllProductsUseCase().collect { products ->
            updateState { it.copy(productList = products) }
        }
    }

    private fun selectProductFromBarcode(barcode: String) {
        state.value.productList.firstOrNull { it.barcode == barcode }?.let {
            handleInvoiceAction(
                BillActions.OnInsertItem(
                    BillItemChart(product = it)
                )
            )
        } ?: run {
            updateState { it.copy(errorBarcode = barcode) }
        }
    }
}