package com.example.ledger.ui.keypad

import androidx.lifecycle.ViewModel
import com.example.ledger.ui.keypad.theme.KeypadTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class KeypadState(
    val displayedValue: String = "0",
    val theme: KeypadTheme = KeypadTheme.GREEN
)

class KeypadViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(KeypadState())
    val uiState: StateFlow<KeypadState> = _uiState

    fun onNumberPress(number: Int) {
        _uiState.value = _uiState.value.copy(
            displayedValue = if (_uiState.value.displayedValue == "0") number.toString() else _uiState.value.displayedValue + number
        )
    }

    fun onBackspacePress() {
        val currentValue = _uiState.value.displayedValue
        if (currentValue.length > 1) {
            _uiState.value = _uiState.value.copy(displayedValue = currentValue.dropLast(1))
        } else {
            _uiState.value = _uiState.value.copy(displayedValue = "0")
        }
    }

    fun onClear() {
        _uiState.value = _uiState.value.copy(displayedValue = "0")
    }

    fun setTheme(theme: KeypadTheme) {
        _uiState.value = _uiState.value.copy(theme = theme)
    }
}