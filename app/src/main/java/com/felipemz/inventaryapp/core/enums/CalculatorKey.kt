package com.felipemz.inventaryapp.core.enums

enum class CalculatorKey(
    val key: String,
    val themeNumber: Int = 0,
) {
    HISTORY("H", 2),
    CLEAR_ALL("AC", 2),
    CLEAR("C", 2),
    DELETE("DEL", 2),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    DIVIDE("÷", 1),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    MULTIPLY("x", 1),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    SUBTRACT("-", 1),
    DOUBLE_ZERO("00"),
    ZERO("0"),
    EQUAL("=", 2),
    ADD("+", 1)
}