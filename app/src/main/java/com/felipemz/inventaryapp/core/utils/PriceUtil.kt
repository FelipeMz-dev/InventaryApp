package com.felipemz.inventaryapp.core.utils

import java.text.NumberFormat
import java.util.Locale

object PriceUtil {

    fun formatPrice(amount: Long, isLess: Boolean = false): String {
        val colombianLocale = Locale("es", "CO")
        val currencyFormatter = NumberFormat.getInstance(colombianLocale)
        val formattedAmount = currencyFormatter.format(amount)
        return "${if (isLess) "- " else String()}$ $formattedAmount"
    }

    fun formatPrice(amount: Int, isLess: Boolean = false): String {
        val colombianLocale = Locale("es", "CO")
        val currencyFormatter = NumberFormat.getInstance(colombianLocale)
        val formattedAmount = currencyFormatter.format(amount)
        return "${if (isLess) "- " else String()}$ $formattedAmount"
    }

    fun getValue(
        price: String,
        default: Int = 0
    ): Int {
        val digits = price.replace(Regex("[^0-9]"), "")
        digits.ifEmpty { return 0 }
        return digits.toIntOrNull() ?: default
    }
}