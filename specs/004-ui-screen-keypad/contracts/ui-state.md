# UI State Contract: Keypad Screen

**Date**: 2025-10-21

This document defines the contract for the UI state of the Keypad screen. The state is managed by a `KeypadViewModel` and exposed to the `KeypadScreen` composable.

## State Object: `KeypadState`

The `KeypadState` is a data class that represents all the information needed to render the UI at any given time.

```kotlin
data class KeypadState(
    val displayedValue: String = "0",
    val theme: KeypadTheme = KeypadTheme.GREEN
)

enum class KeypadTheme {
    GREEN,
    RED
}
```

### Properties

- `displayedValue: String`
  - **Description**: The string representation of the number entered by the user.
  - **Default**: `"0"`

- `theme: KeypadTheme`
  - **Description**: An enum that determines the color scheme of the UI.
  - **Default**: `KeypadTheme.GREEN`

## ViewModel Interface

The `KeypadViewModel` will expose the `KeypadState` and handle user input events.

```kotlin
class KeypadViewModel : ViewModel() {

    // Exposes the UI state to the composable
    val uiState: StateFlow<KeypadState> = // ...

    // Functions to handle user actions
    fun onNumberPress(number: Int) { /* ... */ }
    fun onDecimalPress() { /* ... */ }
    fun onBackspacePress() { /* ... */ }
    fun onClear() { /* ... */ }
    fun setTheme(theme: KeypadTheme) { /* ... */ }
}
```
