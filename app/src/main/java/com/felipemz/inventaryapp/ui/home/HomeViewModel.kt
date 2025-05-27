package com.felipemz.inventaryapp.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.MovementItemModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.core.enums.MovementItemType
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.data.local.entity.ProductEntity
import com.felipemz.inventaryapp.domain.usecase.GetAllProductsUseCase
import com.felipemz.inventaryapp.domain.usecase.InsertProductUseCase
import com.felipemz.inventaryapp.ui.commons.fakeChips
import com.felipemz.inventaryapp.ui.commons.fakeLabelList
import com.felipemz.inventaryapp.ui.commons.fakeMovements
import com.felipemz.inventaryapp.ui.commons.fakeProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val insertProductUseCase: InsertProductUseCase
) : BaseViewModel<HomeState, HomeEvent>() {

    private val _movements = mutableStateOf<List<MovementItemModel>>(emptyList())
    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())

    init {
        _movements.value = fakeMovements
        _products.value = fakeProducts
        orderProducts(ProductsOrderBy.CATEGORY, false)
    }

    override fun initState() = HomeState(
        categories = fakeChips,
        products = _products.value,
        movements = _movements.value,
        movementLabelList = fakeLabelList,
        totalAmount = fakeMovements.sumBy {
            when (it.type) {
                MovementItemType.MOVEMENT_SALE -> it.amount
                MovementItemType.MOVEMENT_EXPENSE -> -it.amount
                else -> 0
            }
        }
    )

    override fun intentHandler() {
        executeIntent { event ->
            when (event) {
                is HomeEvent.OnFocusSearch -> updateState { it.copy(isSearchFocused = event.isFocus) }
                is HomeEvent.OnChangeSearchText -> changeSearchText(event.text)
                is HomeEvent.OnCategorySelected -> categorySelected(event.category)
                is HomeEvent.OnMovementsInverted -> movementsInverted(event.isInverted)
                is HomeEvent.OnMovementFilterSelected -> movementFilterSelected(event.filter)
                is HomeEvent.OnLabelSelected -> labelSelected(event.label)
                is HomeEvent.OnHideLabelPopup -> hideLabelPopup()
                is HomeEvent.OnCloseReportsCalendarPopup -> updateState { it.copy(isReportsCalendarPopup = false) }
                is HomeEvent.OnProductOrderSelected -> orderProducts(event.orderBy, event.isInverted)
                is HomeEvent.OnOpenProductOrderPopup -> updateState { it.copy(isProductOrderPopup = true) }
                is HomeEvent.OpenReportsCalendarPopup -> updateState { it.copy(isReportsCalendarPopup = true) }
                is HomeEvent.OnReportsCustomFilterSelected -> updateState {
                    it.copy(reportsCustomFilterSelected = event.filter, reportsFilterChipSelected = null, isReportsCalendarPopup = false) //TODO: refactor
                }
                is HomeEvent.OnReportsFilterSelected -> updateState {
                    it.copy(reportsFilterChipSelected = event.filter, reportsCustomFilterSelected = null)  //TODO: refactor
                }
                else -> Unit
            }
        }
    }

    private fun loadAllProducts() {   //TODO: use this logic
        //updateState { it.copy(isLoading = true, error = null) }
        execute(Dispatchers.IO) {
            getAllProductsUseCase()
                //.catch { error -> updateState { it.copy(isLoading = false, error = error.message) } }
                .collect { productList ->
                    updateState {
                        it.copy(products = productList, /*isLoading = false, error = null*/)
                    }
                }
        }
    }

    private fun insertProduct(product: ProductModel) {
        execute(Dispatchers.IO) {
            insertProductUseCase(product)
            //eventHandler(ProductEvent.LoadAllProducts) // Refresca productos después de insertar
        }
    }

    private fun orderProducts(     //TODO: Move logic to use case
        orderBy: ProductsOrderBy,
        isInverted: Boolean
    ) {
        updateState {
            it.copy(
                productOrderSelected = orderBy,
                isProductOrderInverted = isInverted,
                isProductOrderPopup = false,
            )
        }
        val sortedProducts = when (orderBy) {
            ProductsOrderBy.CATEGORY -> _products.value.sortedBy {
                fakeChips.findLast { chip -> chip.color == it.category.color }?.position ?: 0
            }
            ProductsOrderBy.NAME -> _products.value.sortedBy { it.name }
            ProductsOrderBy.PRICE -> _products.value.sortedBy { it.price }
            ProductsOrderBy.STOCK -> _products.value.sortedBy { it.quantityChart?.quantity ?: 0 }
            else -> _products.value.sortedBy { it.id }
        }
        val orderedProducts = if (isInverted) sortedProducts.reversed() else sortedProducts
        _products.value = orderedProducts
        when {
            state.value.searchText.isNotEmpty() -> changeSearchText(state.value.searchText)
            state.value.categorySelected.isNotNull() -> categorySelected(state.value.categorySelected)
            else -> updateState { it.copy(products = orderedProducts) }
        }
    }

    private fun changeSearchText(text: String) = execute(Dispatchers.IO) { //TODO: Move logic to use case
        val filteredProducts = _products.value.filter { product ->
            val matchesCategory = state.value.categorySelected.isNull()
                    || product.category.color == state.value.categorySelected?.color
            val matchesSearchText = product.name.contains(text, ignoreCase = true)
            matchesCategory && matchesSearchText
        }
        updateState {
            it.copy(
                products = filteredProducts,
                searchText = text
            )
        }
    }

    private fun categorySelected(category: CategoryModel?) = execute(Dispatchers.IO) { //TODO: Move logic to use case
        val filteredProducts = _products.value.filter { product ->
            val matchesSearchText = state.value.searchText.isEmpty()
                    || product.name.contains(state.value.searchText, ignoreCase = true)
            val matchesCategory = category.isNull() || product.category.color == category?.color
            matchesSearchText && matchesCategory
        }
        updateState { state ->
            state.copy(
                categorySelected = category,
                products = filteredProducts
            )
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
                isShowLabelPopup = false,
                movements = _movements.value.filter { it.labels.contains(label) } //TODO: Move logic to use case
            )
        }
    }

    private fun movementFilterSelected(filter: MovementsFilterChip) {  //TODO: Move logic to use case
        if (filter == MovementsFilterChip.LABEL) {
            updateState { it.copy(isShowLabelPopup = true) }
        } else updateState { it.copy(movementLabelSelected = null) }
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

    private fun hideLabelPopup() {
        updateState {
            it.copy(
                isShowLabelPopup = false,
                movementLabelSelected = null
            )
        }
    }
}