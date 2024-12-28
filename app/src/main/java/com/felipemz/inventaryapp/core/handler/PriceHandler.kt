package com.felipemz.inventaryapp.core.handler

import java.text.NumberFormat
import java.util.Locale

object PriceHandler {

    fun formatPrice(amount: Int, isLess: Boolean = false): String {
        val colombianLocale = Locale("es", "CO")
        val currencyFormatter = NumberFormat.getInstance(colombianLocale)
        val formattedAmount = currencyFormatter.format(amount)
        return "${if (isLess) "- " else String()}$ $formattedAmount"
    }
}