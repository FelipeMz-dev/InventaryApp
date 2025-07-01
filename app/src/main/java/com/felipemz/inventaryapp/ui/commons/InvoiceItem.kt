package com.felipemz.inventaryapp.ui.commons

import com.felipemz.inventaryapp.core.EMPTY_STRING
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.model.ProductTypeImage

abstract class InvoiceItem(open val quantity: Int = 0) {

    abstract fun copy(quantity: Int): InvoiceItem

    abstract fun isEqualTo(other: InvoiceItem): Boolean

    // if id product is 0
    fun hasProduct(): Boolean {
        return when (this) {
            is ProductInvoiceItem -> product.id == 0
            else -> true
        }
    }

    fun toProductInvoice(): ProductInvoiceItem? = when (this) {
        is ProductInvoiceItem -> this
        is AmountInvoiceItem -> ProductInvoiceItem(
            product = ProductModel(
                name = concept,
                price = value,
                image = ProductTypeImage.EmojiImage("\uD83D\uDCB0")
            ),
            quantity = quantity,
        )
        else -> null
    }

    fun toAmountInvoice(): AmountInvoiceItem? = when (this) {
        is AmountInvoiceItem -> this
        is ProductInvoiceItem -> AmountInvoiceItem(
            value = product.price,
            concept = product.name,
            quantity = quantity
        )
        else -> null
    }
}

sealed interface InvoiceActions {

    val item: InvoiceItem

    fun setItem(item: InvoiceItem): InvoiceActions {
        return when (this) {
            is OnInsertItem -> this.copy(item)
            is OnUpdateItem -> this.copy(item)
            is OnRemoveItem -> this.copy(item)
            is OnAddItem -> this.copy(item)
            is OnSubtractItem -> this.copy(item)
        }
    }

    data class OnInsertItem(override val item: InvoiceItem) : InvoiceActions

    data class OnUpdateItem(override val item: InvoiceItem) : InvoiceActions

    data class OnRemoveItem(override val item: InvoiceItem) : InvoiceActions

    data class OnAddItem(override val item: InvoiceItem) : InvoiceActions

    data class OnSubtractItem(override val item: InvoiceItem) : InvoiceActions
}

data class ProductInvoiceItem(
    val product: ProductModel,
    override val quantity: Int = 1,
) : InvoiceItem() {
    override fun copy(quantity: Int) = ProductInvoiceItem(
        product = product,
        quantity = quantity
    )

    override fun isEqualTo(other: InvoiceItem): Boolean {
        return other is ProductInvoiceItem && product.id == other.product.id
    }
}

data class AmountInvoiceItem(
    val value: Int = 0,
    val concept: String = EMPTY_STRING,
    override val quantity: Int = 1,
) : InvoiceItem() {
    override fun copy(quantity: Int) = AmountInvoiceItem(
        concept = concept,
        value = value,
        quantity = quantity
    )

    override fun isEqualTo(other: InvoiceItem): Boolean {
        return other is AmountInvoiceItem && value == other.value && concept == other.concept
    }
}

fun List<InvoiceItem>.getProducts() = filterIsInstance<ProductInvoiceItem>()

fun List<InvoiceItem>.getAmounts() = filterIsInstance<AmountInvoiceItem>()