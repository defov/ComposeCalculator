package com.example.composecalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when(action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Delete -> performDeleting()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> performCalculation()
        }
    }

    private fun enterNumber(number: Int) {
        if(state.operation == null) {
            if(state.number.length >= MAX_NUM_LENGTH) {
                return
            }
            state = state.copy(
                number = state.number + number
            )
            return
        }

        if(state.number2.length >= MAX_NUM_LENGTH) {
            return
        }
        state = state.copy(
            number2 = state.number2 + number
        )
    }

    private fun enterDecimal() {
        if(
            state.operation == null &&
            !state.number.contains(".") &&
            state.number.isNotBlank()
        ) {
            state = state.copy(
                number = state.number + "."
            )
            return
        }

        if(
            !state.number2.contains(".") &&
            state.number2.isNotBlank()
        ) {
            state = state.copy(
                number2 = state.number2 + "."
            )
        }

    }

    private fun performDeleting() {
        when {
            state.number2.isNotBlank() -> state = state.copy(
                number2 = state.number2.dropLast(1)
            )
            state.operation != null -> state = state.copy(
                operation = state.operation
            )
            state.number.isNotBlank() -> state = state.copy(
                number = state.number.dropLast(1)
            )
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if(state.number.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun performCalculation() {
        val number1 = state.number.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        if(number1 != null && number2 != null) {
            val result = when (state.operation) {
                is CalculatorOperation.Add -> number1 + number2
                is CalculatorOperation.Subtract -> number1 - number2
                is CalculatorOperation.Multiply -> number1 * number2
                is CalculatorOperation.Divide -> number1 / number2
                null -> return
            }
            state = state.copy(
                number = result.toString().take(15),
                number2 = "",
                operation = null
            )
        }
    }

    companion object {
        private const val MAX_NUM_LENGTH = 8;
    }
}