package com.felipemz.inventaryapp.ui.movements

import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.entitys.ProductQuantityEntity
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import com.felipemz.inventaryapp.ui.commons.fakeProducts

class MovementsViewModel : BaseViewModel<MovementsState, MovementsEvent>() {

    override fun initState() = MovementsState(
        movementDate = FormatDateUtil.getOfFilterChip(ReportsFilterDate.TODAY),
        productList = fakeProducts
    )

    override fun intentHandler() {
        executeIntent {
            when (it) {
                is MovementsEvent.OnSelectProduct -> selectProduct(it.product)
                is MovementsEvent.OnChangeDiscount -> updateState { uiState ->
                    uiState.copy(discount = it.discount)
                }
                is MovementsEvent.IncrementCalculatorId -> updateState { uiState ->
                    uiState.copy(calculatorId = uiState.calculatorId + 1)
                }
                else -> Unit
            }
        }
    }

    private fun selectProduct(product: ProductQuantityEntity) {
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
                }
            )
        }.invokeOnCompletion { calculateTotal() }
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