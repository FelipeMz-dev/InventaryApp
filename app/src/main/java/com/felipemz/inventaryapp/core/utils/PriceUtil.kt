package com.felipemz.inventaryapp.core.utils

import java.text.NumberFormat
import java.util.Locale

object PriceUtil {

    fun formatPrice(amount: Int, isLess: Boolean = false): String {
        val colombianLocale = Locale("es", "CO")
        val currencyFormatter = NumberFormat.getInstance(colombianLocale)
        val formattedAmount = currencyFormatter.format(amount)
        return "${if (isLess) "- " else String()}$ $formattedAmount"
    }

    fun getValue(
        price: String,
        default: Int = 0
    ) = price.replace(Regex("[^0-9]"), "").toIntOrNull() ?: default
}