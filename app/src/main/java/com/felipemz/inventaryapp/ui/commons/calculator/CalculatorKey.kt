package com.felipemz.inventaryapp.ui.commons.calculator

enum class CalculatorKey(
    val key: String,
    val themeNumber: Int = 0,
) {
    HISTORY("H", 1),
    CLEAR_ALL("AC", 1),
    CLEAR("C", 1),
    DELETE("DEL", 1),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    ADD("+", 3),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SUBTRACT("-", 3),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    MULTIPLY("x", 2),
    DOUBLE_ZERO("00"),
    ZERO("0"),
    EQUAL("=", 1),
    DIVIDE("÷", 2);

    fun isOperator(): Boolean {
        return this == ADD || this == SUBTRACT || this == MULTIPLY || this == DIVIDE
    }

    fun isNumber(): Boolean {
        return this != ADD && this != SUBTRACT && this != MULTIPLY && this != DIVIDE &&
                this != CLEAR && this != CLEAR_ALL && this != DELETE && this != EQUAL &&
                this != HISTORY
    }

    fun isAddOrSubtract(): Boolean {
        return this == ADD || this == SUBTRACT
    }

    fun isMultiplyOrDivide(): Boolean {
        return this == MULTIPLY || this == DIVIDE
    }
}