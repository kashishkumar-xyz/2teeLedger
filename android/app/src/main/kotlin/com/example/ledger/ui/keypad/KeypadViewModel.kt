package com.example.ledger.ui.keypad

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class KeypadTheme {
    GREEN,
    RED
}

data class KeypadState(
    val displayText: String = "0",
    val theme: KeypadTheme = KeypadTheme.GREEN
)

sealed class KeypadEvent {
    data class Number(val number: Int) : KeypadEvent()
    object Decimal : KeypadEvent()
    object Backspace : KeypadEvent()
    object Clear : KeypadEvent()
    data class ThemeChange(val theme: KeypadTheme) : KeypadEvent()
}

class KeypadViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(KeypadState())
    val uiState: StateFlow<KeypadState> = _uiState.asStateFlow()

    fun onEvent(event: KeypadEvent) {
        when (event) {
            is KeypadEvent.Number -> {
                val currentText = _uiState.value.displayText
                val newText = if (currentText == "0") {
                    event.number.toString()
                } else {
                    currentText + event.number
                }
                _uiState.value = _uiState.value.copy(displayText = newText)
            }
            KeypadEvent.Backspace -> {
                val currentText = _uiState.value.displayText
                if (currentText.length > 1) {
                    _uiState.value = _uiState.value.copy(displayText = currentText.dropLast(1))
                } else {
                    _uiState.value = _uiState.value.copy(displayText = "0")
                }
            }
            KeypadEvent.Clear -> {
                _uiState.value = _uiState.value.copy(displayText = "0")
            }
            is KeypadEvent.ThemeChange -> {
                _uiState.value = _uiState.value.copy(theme = event.theme)
            }
            else -> {}
        }
    }
}
