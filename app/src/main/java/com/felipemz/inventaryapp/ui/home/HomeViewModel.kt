package com.felipemz.inventaryapp.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.charts.RangeDateChart
import com.felipemz.inventaryapp.core.enums.MovementItemType
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.domain.model.MovementModel
import com.felipemz.inventaryapp.domain.usecase.GetProductFromBarcodeUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveAllCategoriesUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveAllProductsUseCase
import com.felipemz.inventaryapp.domain.usecase.SortProductsFromObserver
import com.felipemz.inventaryapp.ui.commons.delegate.ProductListFilterDelegate
import com.felipemz.inventaryapp.ui.commons.delegate.ProductListFilterDelegateImpl
import com.felipemz.inventaryapp.ui.commons.fakeLabelList
import com.felipemz.inventaryapp.ui.commons.fakeMovements
import com.felipemz.inventaryapp.ui.home.HomeEvent.Init
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnCreateProductFromBarcode
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnFocusSearch
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnLabelChangeToShow
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnLabelSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnMovementFilterSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnMovementsInverted
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnProductOrderSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnProductsOrderChangeToShow
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnReportsCalendarChangeToShow
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnReportsCustomFilterSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnReportsFilterSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnScannerChangeToShow
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnSetCategoryFilterProducts
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnSetNameFilterProducts
import kotlinx.coroutines.Dispatchers

class HomeViewModel(
    private val observeAllProductsUseCase: ObserveAllProductsUseCase,
    private val sortProductsFromObserver: SortProductsFromObserver,
    private val observeAllCategoriesUseCase: ObserveAllCategoriesUseCase,
    private val getProductFromBarcodeUseCase: GetProductFromBarcodeUseCase,
) : BaseViewModel<HomeState, HomeEvent>() {

    private val action = MutableLiveData<HomeAction>()
    val actionLiveData: LiveData<HomeAction> = action

    private val _movements = mutableStateOf<List<MovementModel>>(emptyList())

    private val productsFilteredDelegate: ProductListFilterDelegate = ProductListFilterDelegateImpl()

    val productList = productsFilteredDelegate.filteredProductList

    init {
        _movements.value = fakeMovements
        setOrderProducts(ProductsOrderBy.CATEGORY, false)
    }

    override fun initState() = HomeState(
        movements = _movements.value,
        movementLabelList = fakeLabelList,
        totalAmount = fakeMovements.sumBy {
            when (it.type) {
                MovementItemType.MOVEMENT_SALE -> it.total
                MovementItemType.MOVEMENT_EXPENSE -> -it.total
                else -> 0
            }
        }
    )

    override fun intentHandler() {
        executeIntent { event ->
            when (event) {
                is Init -> onInit()
                is OnMovementsInverted -> movementsInverted(event.isInverted)
                is OnMovementFilterSelected -> movementFilterSelected(event.filter)
                is OnLabelSelected -> labelSelected(event.label)
                is OnProductOrderSelected -> setOrderProducts(event.orderBy, event.isInverted)
                is OnReportsCustomFilterSelected -> reportsCustomFilterSelected(event.filter)
                is OnReportsFilterSelected -> reportsFilterSelected(event.filter)
                is OnSetNameFilterProducts -> productsFilteredDelegate.setFilterName(event.name)
                is OnSetCategoryFilterProducts -> setCategoryFilterProducts(event)
                is OnFocusSearch -> updateState { it.copy(isSearchFocused = event.isFocus) }
                is OnCreateProductFromBarcode -> verifyBarcodeToCreateProduct(event.barcode)
                is OnLabelChangeToShow -> updateState { it.copy(isShowLabelPopup = event.isShow) }
                is OnScannerChangeToShow -> updateState { it.copy(showScanner = event.isShow) }
                is OnProductsOrderChangeToShow -> updateState { it.copy(showProductOrderDialog = event.isShow) }
                is OnReportsCalendarChangeToShow -> updateState { it.copy(showReportsCalendarDialog = event.isShow) }
                else -> Unit
            }
        }
    }

    private fun onInit() {
        observeAllCategories()
        observeAllProducts()
    }

    private fun observeAllCategories() = execute(Dispatchers.IO) {
        observeAllCategoriesUseCase().collect { categories ->
            updateState { it.copy(categories = categories) }
        }
    }

    private fun observeAllProducts() = execute(Dispatchers.IO) {
        observeAllProductsUseCase().collect { products ->
            productsFilteredDelegate.setProductList(products)
        }
    }

    private fun reportsCustomFilterSelected(filter: RangeDateChart) {
        updateState { state ->
            state.copy(
                reportsCustomFilterSelected = filter,
                reportsFilterChipSelected = null,
                showReportsCalendarDialog = false,
            )
        }
    }

    private fun reportsFilterSelected(filter: ReportsFilterDate) {
        updateState { state ->
            state.copy(
                reportsFilterChipSelected = filter,
                reportsCustomFilterSelected = null,
            )
        }
    }

    private fun setCategoryFilterProducts(event: OnSetCategoryFilterProducts) {
        productsFilteredDelegate.setFilterCategory(event.category)
        updateState { state -> state.copy(categorySelected = event.category) }
    }

    private fun setOrderProducts(
        orderBy: ProductsOrderBy,
        isInverted: Boolean
    ) {
        updateState {
            it.copy(
                productOrderSelected = orderBy,
                isProductOrderInverted = isInverted,
                showProductOrderDialog = false,
            ).also {
                sortProductsFromObserver(orderBy, isInverted)
            }
        }
    }

    private fun movementsInverted(isInverted: Boolean) {   //TODO: Move logic to use case
        if (state.value.isMovementsInverted != isInverted) {
            updateState {
                it.copy(
                    isMovementsInverted = isInverted,
                    movements = it.movements.reversed()
                )
            }
        }
    }

    private fun labelSelected(label: String) {
        updateState { state ->
            state.copy(
                movementLabelSelected = label,
                movements = _movements.value.filter { it.labels.contains(label) } //TODO: Move logic to use case
            )
        }
    }

    private fun movementFilterSelected(filter: MovementsFilterChip) {  //TODO: Move logic to use case
        if (filter != MovementsFilterChip.LABEL) {
            updateState { it.copy(movementLabelSelected = null) }
        }
        updateState { state ->
            state.copy(
                movementFilterSelected = filter,
                movements = _movements.value.filter {
                    when (filter) {
                        MovementsFilterChip.ALL -> true
                        MovementsFilterChip.INCOME -> it.type == MovementItemType.MOVEMENT_SALE
                        MovementsFilterChip.EXPENSE -> it.type == MovementItemType.MOVEMENT_EXPENSE
                        MovementsFilterChip.PENDING -> it.type == MovementItemType.MOVEMENT_PENDING
                        MovementsFilterChip.LABEL -> it.labels.isEmpty()
                    }
                }
            )
        }
    }

    private fun verifyBarcodeToCreateProduct(barcode: String) = execute(Dispatchers.IO) {
        getProductFromBarcodeUseCase(barcode)?.let {
            action.postValue(HomeAction.OpenProduct(it.id))
        } ?: run {
            action.postValue(HomeAction.CreateProductFromBarcode(barcode))
        }

    }
}