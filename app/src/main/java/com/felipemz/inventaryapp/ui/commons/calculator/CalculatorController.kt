package com.felipemz.inventaryapp.ui.commons.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.extensions.tryOrDefault
import kotlin.collections.plus
import kotlin.takeUnless

class CalculatorController(initialQuantity: Int) {
    var value by mutableIntStateOf(initialQuantity)
    var listOperation by mutableStateOf(listOf<MathOperation>())
    var cursorPosition by mutableIntStateOf(1)
    var showHistory by mutableStateOf(false)
    var result by mutableStateOf(0 to CalculatorKey.EQUAL)
    var temporalResult by mutableStateOf<Int?>(null)
    var temporalOperator by mutableStateOf<CalculatorKey?>(null)
    val isEnableCalculator: Boolean
        get() = !(temporalResult != null && temporalOperator == null)

    fun onKeyPress(key: CalculatorKey) {
        when {
            key.isNumber() -> insertNumber(key)
            key.isOperator() -> applyOperator(key)
            key == CalculatorKey.EQUAL -> applyOperator(key)
            key == CalculatorKey.HISTORY -> showHistory = !showHistory
            key == CalculatorKey.CLEAR -> clearValue()
            key == CalculatorKey.CLEAR_ALL -> reset()
            key == CalculatorKey.DELETE -> deleteLastDigit()
        }
    }

    private fun insertNumber(key: CalculatorKey) {
        value = tryOrDefault(value) {
            if (cursorPosition != value.toString().length) {
                StringBuilder(value.toString()).insert(cursorPosition, key.key).toString().toInt()
            } else {
                (value.toString() + key.key).toInt()
            }.also {
                cursorPosition += key.key.length.takeUnless { value == 0 } ?: 0
            }
        }
    }

    private fun applyOperator(operator: CalculatorKey) {
        if (temporalOperator.isNotNull() && value != 0) {
            value = calculate(temporalResult ?: 0, value, temporalOperator!!)
            temporalResult = null
            temporalOperator = null
        }
        when {
            result.first == 0 && listOperation.isEmpty() && value == 0 && temporalResult.isNull() -> return
            operator.isMultiplyOrDivide() -> handleMultiplyDivide(operator)
            else -> handleAddSubtractOrEqual(operator)
        }
        clearValue()
    }

    private fun handleMultiplyDivide(operator: CalculatorKey) {
        if (value != 0) {
            if (temporalOperator.isNull() && temporalResult.isNull()) {
                temporalResult = value
                temporalOperator = operator
            } else addOperation(operator)
        } else {
            if (temporalOperator.isNull()) {
                result = result.copy(second = operator)
                temporalResult = null
            } else {
                temporalOperator = operator
            }
        }
    }

    private fun handleAddSubtractOrEqual(operator: CalculatorKey) {
        temporalResult?.let {
            temporalOperator = null
            value = it
        }
        if (value != 0) addOperation(operator)
        temporalResult = result.first.takeIf { !operator.isOperator() }
        result = result.copy(second = operator)
    }

    private fun addOperation(operator: CalculatorKey) {
        if (result.second.isOperator()) {
            listOperation = listOperation + MathOperation(
                first = result.first,
                second = value,
                operator = result.second
            ).apply { operate() }
        }
        result = CalculatorResult(listOperation.lastOrNull()?.result ?: value, operator)
    }

    private fun calculate(
        a: Int,
        b: Int,
        op: CalculatorKey
    ): Int = when (op) {
        CalculatorKey.MULTIPLY -> a * b
        CalculatorKey.DIVIDE -> if (b != 0) a / b else 0
        CalculatorKey.ADD -> a + b
        CalculatorKey.SUBTRACT -> a - b
        else -> b
    }

    private fun deleteLastDigit() {
        if (value == 0 && temporalOperator.isNotNull()) {
            leaveTemporal()
            return
        }
        value = value.toString().dropLast(1).toIntOrNull() ?: 0
        cursorPosition = (cursorPosition - 1).coerceAtLeast(1)
    }

    private fun leaveTemporal() = temporalResult?.let {
            value = it
            cursorPosition = it.toString().length
            temporalResult = null
            temporalOperator = null
    }

    fun clearValue() {
        value = 0
        cursorPosition = 1
    }

    fun reset() {
        clearValue()
        listOperation = emptyList()
        temporalResult = null
        temporalOperator = null
        result = 0 to CalculatorKey.EQUAL
    }
}