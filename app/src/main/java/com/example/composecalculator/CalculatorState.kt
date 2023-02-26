package com.example.composecalculator

data class CalculatorState(
    val number: String = "",
    val number2: String = "",
    val operation: CalculatorOperation? = null
)
