package com.example.ledger.ui.keypad

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale

enum class KeypadTheme {
    GREEN,
    RED
}

data class KeypadState(
    val displayText: String = "0",
    val rawText: String = "0",
    val theme: KeypadTheme = KeypadTheme.GREEN
)

sealed class KeypadEvent {
    data class Number(val number: Int) : KeypadEvent()
    object Decimal : KeypadEvent()
    object Backspace : KeypadEvent()
    object Clear : KeypadEvent()
    object Reset : KeypadEvent()
    data class ThemeChange(val theme: KeypadTheme) : KeypadEvent()
}

class KeypadViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(KeypadState())
    val uiState: StateFlow<KeypadState> = _uiState.asStateFlow()

    private fun formatNumber(number: String): String {
        // Handle cases where the string might be empty or just a "-"
        if (number.isEmpty() || number == "-") {
            return number
        }
        // Remove existing commas to parse the number correctly
        val cleanString = number.replace(",", "")
        return try {
            val parsed = cleanString.toLong()
            // Using NumberFormat for better locale support
            NumberFormat.getNumberInstance(Locale.US).format(parsed)
        } catch (e: NumberFormatException) {
            // If it's not a valid number (e.g., during input), return the raw text
            uiState.value.rawText
        }
    }

    fun onEvent(event: KeypadEvent) {
        when (event) {
            is KeypadEvent.Number -> {
                val newRawText = if (_uiState.value.rawText == "0") {
                    event.number.toString()
                } else {
                    _uiState.value.rawText + event.number
                }
                _uiState.value = _uiState.value.copy(
                    rawText = newRawText,
                    displayText = formatNumber(newRawText)
                )
            }
            KeypadEvent.Backspace -> {
                val newRawText = if (_uiState.value.rawText.length > 1) {
                    _uiState.value.rawText.dropLast(1)
                } else {
                    "0"
                }
                _uiState.value = _uiState.value.copy(
                    rawText = newRawText,
                    displayText = formatNumber(newRawText)
                )
            }
            KeypadEvent.Clear -> {
                _uiState.value = KeypadState(theme = _uiState.value.theme)
            }
            is KeypadEvent.ThemeChange -> {
                _uiState.value = _uiState.value.copy(theme = event.theme)
            }
            KeypadEvent.Reset -> {
                _uiState.value = KeypadState()
            }
            else -> {}
        }
    }
}
