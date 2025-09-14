package com.felipemz.inventaryapp.ui.commons.delegate

import com.felipemz.inventaryapp.core.charts.BillItemChart

interface InvoiceItemsDelegate {
    fun insertInvoiceItem(item: BillItemChart)
    fun addInvoiceItem(item: BillItemChart)
    fun subtractInvoiceItem(item: BillItemChart)
    fun removeInvoiceItem(item: BillItemChart)
    fun updateInvoiceItem(item: BillItemChart)
}

class InvoiceItemsDelegateImpl(
    private val getBillList: () -> List<BillItemChart>,
    private val updateBillList: (List<BillItemChart>) -> Unit
) : InvoiceItemsDelegate {
    private fun BillItemChart.isEqualTo(item: BillItemChart): Boolean {
        return item.product?.let { product?.id == it.id } ?: run {
            concept == item.concept && value == item.value
        }
    }

    override fun insertInvoiceItem(item: BillItemChart) {
        val billList = getBillList()
        if (billList.any { it.isEqualTo(item) }) addInvoiceItem(item)
        else updateBillList(billList + item)
    }

    override fun addInvoiceItem(item: BillItemChart) {
        val billList = getBillList()
        if (billList.any { it.isEqualTo(item) }) {
            updateBillList(
                billList.map {
                    if (it.isEqualTo(item)) it.copy(quantity = it.quantity + 1) else it
                }
            )
        }
    }

    override fun subtractInvoiceItem(item: BillItemChart) {
        val billList = getBillList()
        if (billList.any { it.isEqualTo(item) }) {
            updateBillList(
                billList.mapNotNull {
                    if (it.isEqualTo(item)) {
                        val newValue = it.quantity - 1
                        it.copy(quantity = newValue).takeIf { newValue > 0 }
                    } else it
                }
            )
        }
    }

    override fun removeInvoiceItem(item: BillItemChart) {
        val billList = getBillList()
        if (billList.any { it.isEqualTo(item) }) {
            updateBillList(billList.filterNot { it.isEqualTo(item) })
        }
    }

    override fun updateInvoiceItem(item: BillItemChart) {
        val billList = getBillList()
        if (billList.any { it.isEqualTo(item) }) {
            updateBillList(
                billList.map {
                    if (it.isEqualTo(item)) item else it
                }
            )
        }
    }
}