package com.felipemz.inventaryapp.ui.movements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.charts.BillItemChart
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import com.felipemz.inventaryapp.domain.usecase.GetProductFromBarcodeUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveAllCategoriesUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveAllProductsUseCase
import com.felipemz.inventaryapp.ui.commons.actions.BillActions
import com.felipemz.inventaryapp.ui.commons.delegate.InvoiceItemsDelegate
import com.felipemz.inventaryapp.ui.commons.delegate.InvoiceItemsDelegateImpl
import com.felipemz.inventaryapp.ui.commons.delegate.ProductListFilterDelegate
import com.felipemz.inventaryapp.ui.commons.delegate.ProductListFilterDelegateImpl
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductEvent
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.*
import kotlinx.coroutines.Dispatchers

class MovementsViewModel(
    private val observeAllProductsUseCase: ObserveAllProductsUseCase,
    private val observeAllCategoriesUseCase: ObserveAllCategoriesUseCase,
    private val getProductFromBarcodeUseCase: GetProductFromBarcodeUseCase
) : BaseViewModel<MovementsState, MovementsEvent>() {

    private val action = MutableLiveData<MovementsAction>()
    val actionLiveData: LiveData<MovementsAction> = action

    private val invoiceDelegate: InvoiceItemsDelegate = InvoiceItemsDelegateImpl(
        getBillList = { state.value.billList },
        updateBillList = { newList -> updateState { it.copy(billList = newList) } }
    )

    private val productsFilteredDelegate: ProductListFilterDelegate = ProductListFilterDelegateImpl()

    val productList = productsFilteredDelegate.filteredProductList

    override fun initState() = MovementsState(
        movementDate = FormatDateUtil.getOfFilterChip(ReportsFilterDate.TODAY),
    )

    override fun intentHandler() {
        executeIntent { event ->
            when (event) {
                is Init -> init()
                is OnClearBarcodeError -> clearBarcodeError()
                is OnSelectProductFromBarcode -> selectProductFromBarcode(event.barcode)
                is OnChangeDiscount -> changeDiscount(event.discount)
                is OnExecuteAction -> action.postValue(event.action)
                is OnInvoiceAction -> handleInvoiceAction(event.action)
                is OnSetNameFilter -> productsFilteredDelegate.setFilterName(event.name)
                is OnSetCategoryFilter -> productsFilteredDelegate.setFilterCategory(event.category)
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

    private fun init() {
        observeAllProducts()
        observeAllCategories()
    }

    private fun observeAllProducts() = execute(Dispatchers.IO) {
        observeAllProductsUseCase().collect { products ->
            productsFilteredDelegate.setProductList(products)
        }
    }

    private fun observeAllCategories() = execute(Dispatchers.IO) {
        observeAllCategoriesUseCase().collect { categories ->
            updateState { it.copy(categories = categories) }
        }
    }

    private fun clearBarcodeError() {
        updateState { it.copy(errorBarcode = null) }
    }

    private fun changeDiscount(discount: Int) {
        updateState { it.copy(discount = discount) }
    }

    private fun selectProductFromBarcode(barcode: String) = execute(Dispatchers.IO) {
        getProductFromBarcodeUseCase(barcode)?.let { product ->
            handleInvoiceAction(
                BillActions.OnInsertItem(
                    BillItemChart(product = product)
                )
            )
        } ?: run {
            updateState { it.copy(errorBarcode = barcode) }
        }
    }
}