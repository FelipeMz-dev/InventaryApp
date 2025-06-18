package com.felipemz.inventaryapp

fun main(){
    val calculatorResult = "1 + 1 + 2 - 4 + 2"
    fun operate(expression: String): Int {
        val tokens = expression.split(" ")
        var result = 0
        var currentOperator: String? = null

        for (token in tokens) {
            when {
                token.toIntOrNull() != null -> {
                    val number = token.toInt()
                    if (currentOperator == null) {
                        result = number
                    } else {
                        result = when (currentOperator) {
                            "+" -> result + number
                            "-" -> result - number
                            "*" -> result * number
                            "/" -> result / number
                            else -> result
                        }
                    }
                }
                token in listOf("+", "-", "*", "/") -> {
                    currentOperator = token
                }
            }
        }
        return result
    }
    println("Result: ${operate(calculatorResult)}")
}