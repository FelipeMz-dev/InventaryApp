package com.felipemz.inventaryapp.core.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtil {

    private val colombianLocale = Locale("es", "CO")
    private var currencyFormatter = NumberFormat.getCurrencyInstance(colombianLocale)

    fun formatPrice(amount: Long, isLess: Boolean = false): String {
        val currencyFormatter = NumberFormat.getInstance(colombianLocale)
        val formattedAmount = currencyFormatter.format(amount)
        return "${if (isLess) "- " else String()}$ $formattedAmount"
    }

    fun formatPrice(amount: Int, isLess: Boolean = false): String {
        val currencyFormatter = NumberFormat.getInstance(colombianLocale)
        val formattedAmount = currencyFormatter.format(amount)
        return "${if (isLess) "- " else String()}$ $formattedAmount"
    }

    fun formatWithoutCurrency(amount: Int): String {
        val currencyFormatter = NumberFormat.getInstance(colombianLocale)
        return currencyFormatter.format(amount)
    }

    fun setCurrencyFormatter(locale: Locale) {
        currencyFormatter = NumberFormat.getCurrencyInstance(locale)
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