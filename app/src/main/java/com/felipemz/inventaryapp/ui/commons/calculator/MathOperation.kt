package com.felipemz.inventaryapp.ui.commons.calculator

data class MathOperation(
    val first: Int,
    val second: Int,
    val operator: CalculatorKey?
) {

    private var _result: Int? = null
    val result: Int?
        get() = _result

    fun hasResult(): Boolean = _result != null

    fun operate() {
        _result = when (operator) {
            CalculatorKey.ADD -> first + second
            CalculatorKey.SUBTRACT -> first - second
            CalculatorKey.MULTIPLY -> first * second
            CalculatorKey.DIVIDE -> if (second != 0) first / second else 0
            else -> first
        }
    }
}