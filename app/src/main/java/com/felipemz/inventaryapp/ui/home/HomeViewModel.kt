package com.felipemz.inventaryapp.ui.home

import androidx.compose.runtime.mutableStateOf
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.charts.RangeDateChart
import com.felipemz.inventaryapp.core.enums.MovementItemType
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.extensions.orEmpty
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.MovementModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.usecase.ObserveAllCategoriesUseCase
import com.felipemz.inventaryapp.domain.usecase.ObserveAllProductsUseCase
import com.felipemz.inventaryapp.domain.usecase.SortProductsFromObserver
import com.felipemz.inventaryapp.ui.commons.delegate.ProductListFilterDelegate
import com.felipemz.inventaryapp.ui.commons.delegate.ProductListFilterDelegateImpl
import com.felipemz.inventaryapp.ui.commons.fakeLabelList
import com.felipemz.inventaryapp.ui.commons.fakeMovements
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductEvent
import com.felipemz.inventaryapp.ui.home.HomeEvent.*
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnSetCategoryFilter
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnSetNameFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel(
    private val observeAllProductsUseCase: ObserveAllProductsUseCase,
    private val sortProductsFromObserver: SortProductsFromObserver,
    private val observeAllCategoriesUseCase: ObserveAllCategoriesUseCase,
) : BaseViewModel<HomeState, HomeEvent>() {

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
                is OnSetCategoryFilterProducts -> productsFilteredDelegate.setFilterCategory(event.category)
                is OnFocusSearch -> updateState { it.copy(isSearchFocused = event.isFocus) }
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
                reportsFilterChipSelected = null
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

    private fun setOrderProducts(
        orderBy: ProductsOrderBy,
        isInverted: Boolean
    ) {
        updateState {
            it.copy(
                productOrderSelected = orderBy,
                isProductOrderInverted = isInverted,
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
}